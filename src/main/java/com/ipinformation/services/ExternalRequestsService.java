package com.ipinformation.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipinformation.exceptions.ApiErrorException;
import com.ipinformation.model.responses.DevMeCountryApiResponse;
import com.ipinformation.model.responses.DevMeCurrencyApiResponse;
import com.ipinformation.model.responses.IPApiResponse;

import com.ipinformation.model.responses.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

@Slf4j
@Service
public class ExternalRequestsService {

    @Value(value="${ipapi.endpoint}")
    private String ipapiEndpoint;

    @Value(value="${ipapi.key}")
    private String ipapiAccessKey;

    @Value(value="${dev.me.endpoint}")
    private String devMeEmdpoint;

    @Value(value="${dev.me.api.key}")
    private String devMeAccessKey;

    private final DbService dbService;

    private final RestClient restClient;

    private final MessageSource messageSource;

    private final ObjectMapper objectMapper;

    public ExternalRequestsService(DbService dbService, RestClient restClient, MessageSource messageSource, ObjectMapper objectMapper) {
        this.dbService = dbService;
        this.restClient = restClient;
        this.messageSource = messageSource;
        this.objectMapper = objectMapper;
    }

    private <T extends Response> void checkResponse(T response) {
        try {
            Class<?> clazz = Class.forName(response.getClass().getName());
            Constructor<?> ctor = clazz.getConstructor();
            Object emptyObject = ctor.newInstance();

            String emptyObjectString = objectMapper.writeValueAsString(emptyObject);
            String responseString = objectMapper.writeValueAsString(response);
            boolean condition = emptyObjectString.equals(responseString);
            if (condition || response.getError() != null) {
                log.warn(messageSource.getMessage("error.empty.api.response", null, Locale.getDefault()));

                String errorMessage = messageSource.getMessage("error.internal", null, Locale.getDefault());
                throw new ApiErrorException(condition || response.getError() == null ? errorMessage : response.getError().getInfo());
            }
        }
        catch (ClassNotFoundException | JsonProcessingException | InstantiationException | IllegalAccessException |
               InvocationTargetException | NoSuchMethodException e) {
            log.error(messageSource.getMessage("error.unknown", null, Locale.getDefault()), e);
            throw new ApiErrorException(messageSource.getMessage("error.internal", null, Locale.getDefault()));
        }

    }

    public IPApiResponse getIpInfo(String ipAddress) throws RestClientException, ApiErrorException {
        IPApiResponse response = dbService.getFromDb(ipAddress, IPApiResponse.class);

        if (response != null) {
            return response;
        }

        String ipApiUrl = ipapiEndpoint + ipAddress + "?access_key=" + ipapiAccessKey + "&fields=country_name,country_code,location.languages";

        response = restClient.get().uri(ipApiUrl).retrieve().body(IPApiResponse.class);

        response.setIp(ipAddress);

        checkResponse(response);

        dbService.saveToDbAsync(response);

        return response;
    }

    @Cacheable(value = "COUNTRY_INFO", key = "#countryCode")
    public DevMeCountryApiResponse getCountryInfo(String countryCode) throws RestClientException, ApiErrorException {
        DevMeCountryApiResponse response = dbService.getFromDb(countryCode, DevMeCountryApiResponse.class);

        if (response != null) {
            return response;
        }
        String countryApiUrl =  devMeEmdpoint + "/v1-get-country-details?code=" + countryCode + "&x-api-key=" + devMeAccessKey;

        response = restClient.get().uri(countryApiUrl).retrieve().body(DevMeCountryApiResponse.class);

        checkResponse(response);

        dbService.saveToDbAsync(response);

        return response;
    }

    @Cacheable(value = "CURRENCY_INFO", key = "#currencyCode")
    public DevMeCurrencyApiResponse getCurrencyInfo(String currencyCode) throws RestClientException, ApiErrorException {
        String fixerApiUrl =  devMeEmdpoint + "/v1-get-currency-exchange-rate?from=" + currencyCode + "&to=USD" + "&x-api-key=" + devMeAccessKey;

        DevMeCurrencyApiResponse response = restClient.get().uri(fixerApiUrl).retrieve().body(DevMeCurrencyApiResponse.class);

        checkResponse(response);

        return response;
    }
}
