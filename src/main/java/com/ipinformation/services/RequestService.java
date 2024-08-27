package com.ipinformation.services;

import com.ipinformation.model.responses.DevMeCountryApiResponse;
import com.ipinformation.model.responses.DevMeCurrencyApiResponse;
import com.ipinformation.model.responses.IPApiResponse;
import com.ipinformation.model.responses.IpLookupResponse;
import com.ipinformation.utils.DistanceCalculator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class RequestService {


    private final ExternalRequestsService externalRequestsService;

    private final DistanceCalculator distanceCalculator = new DistanceCalculator();

    public RequestService(ExternalRequestsService externalRequestsService) {
        this.externalRequestsService = externalRequestsService;
    }

    @Cacheable(value = "RESPONSE_OBJECT", key = "#ipAddress")
    public IpLookupResponse getResponse(String ipAddress) {

        IPApiResponse ipInfo = externalRequestsService.getIpInfo(ipAddress);
        DevMeCountryApiResponse countryInfo = externalRequestsService.getCountryInfo(ipInfo.getCountryCode());
        DevMeCurrencyApiResponse currencyInfo = externalRequestsService.getCurrencyInfo(countryInfo.getFirstCurrency().getCode());
        double distance = distanceCalculator.calculateDistanceFromBA(countryInfo.getLatitude(), countryInfo.getLongitude());

        IpLookupResponse response = new IpLookupResponse();

        response.setCountryCode(ipInfo.getCountryCode());
        response.setCountryName(ipInfo.getCountryName());
        response.setIp(ipAddress);
        response.setDistanceToBA(distance);
        response.setCurrencyRate(currencyInfo.getExchangeRate());
        response.setCurrency(countryInfo.getFirstCurrency());
        response.setLanguages(ipInfo.getLocation().getLanguages());
        response.setTimeZones(countryInfo.getTimezones());

        return response;
    }

}
