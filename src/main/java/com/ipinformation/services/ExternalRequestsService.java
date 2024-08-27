package com.ipinformation.services;

import com.ipinformation.model.responses.DevMeCountryApiResponse;
import com.ipinformation.model.responses.DevMeCurrencyApiResponse;
import com.ipinformation.model.responses.IPApiResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

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

    private final MongoTemplate mongoDb;

    private final RestClient restClient;

    public ExternalRequestsService(MongoTemplate mongoDb, DbService dbService, RestClient restClient) {
        this.mongoDb = mongoDb;
        this.dbService = dbService;
        this.restClient = restClient;
    }

    public IPApiResponse getIpInfo(String ipAddress) {
        IPApiResponse response = mongoDb.findById(ipAddress, IPApiResponse.class);

        if (response != null) {
            return response;
        }

        String ipApiUrl = ipapiEndpoint + ipAddress + "?access_key=" + ipapiAccessKey + "&fields=country_name,country_code,location.languages";

        response = restClient.get().uri(ipApiUrl).retrieve().body(IPApiResponse.class);

        dbService.saveToDbAsync(response);

        return response;
    }

    @Cacheable(value = "COUNTRY_INFO", key = "#countryCode")
    public DevMeCountryApiResponse getCountryInfo(String countryCode) {
        DevMeCountryApiResponse response = mongoDb.findById(countryCode, DevMeCountryApiResponse.class);

        if (response != null) {
            return response;
        }

        String countryApiUrl =  devMeEmdpoint + "/v1-get-country-details?code=" + countryCode + "&x-api-key=" + devMeAccessKey;

        response = restClient.get().uri(countryApiUrl).retrieve().body(DevMeCountryApiResponse.class);

        dbService.saveToDbAsync(response);
        return response;
    }

    @Cacheable(value = "CURRENCY_INFO", key = "#currencyCode")
    public DevMeCurrencyApiResponse getCurrencyInfo(String currencyCode) {
        String fixerApiUrl =  devMeEmdpoint + "/v1-get-currency-exchange-rate?from=" + currencyCode + "&to=USD" + "&x-api-key=" + devMeAccessKey;

        return restClient.get().uri(fixerApiUrl).retrieve().body(DevMeCurrencyApiResponse.class);
    }
}
