package com.ipinformation.model.responses;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ipinformation.model.objects.Location;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document("IPApiResponse")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IPApiResponse extends Response implements Serializable {

    @Id
    @JsonAlias("ip")
    private String ip;
    private String countryCode;
    private String countryName;
    private Location location;

    public String getIp() {
        return ip;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Location getLocation() {
        return location;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
