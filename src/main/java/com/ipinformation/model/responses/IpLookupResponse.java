package com.ipinformation.model.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ipinformation.model.objects.Currency;
import com.ipinformation.model.objects.Language;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

@Document("IpLookupResponse")
public class IpLookupResponse {
    private String ip;
    private String countryCode;
    private String countryName;
    private double distanceToBA;
    private Currency currency;
    private double currencyRate;
    private List<Language> languages;
    @JsonIgnore
    @Transient
    private SortedMap<String, LocalDateTime> localTimes;
    private List<String> timeZones;

    public String getIp() {
        return ip;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public double getDistanceToBA() {
        return distanceToBA;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public SortedMap<String, LocalDateTime> getLocalTimes() {
        return localTimes;
    }

    public List<String> getTimeZones() {
        return timeZones;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setDistanceToBA(double distanceToBA) {
        this.distanceToBA = distanceToBA;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public void setLocalTimes(SortedMap<String, LocalDateTime> localTimes) {
        this.localTimes = localTimes;
    }

    public void setCurrencyRate(double currencyRate) {
        this.currencyRate = currencyRate;
    }

    public double getCurrencyRate() {
        return currencyRate;
    }

    public void setTimeZones(List<String> timeZones) {
        this.timeZones = timeZones;
    }

}
