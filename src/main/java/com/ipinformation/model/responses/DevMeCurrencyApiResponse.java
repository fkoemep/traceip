package com.ipinformation.model.responses;

import java.io.Serializable;

public class DevMeCurrencyApiResponse implements Serializable {
    private String from;
    private double exchangeRate;

    public double getExchangeRate() {
        return exchangeRate;
    }
}
