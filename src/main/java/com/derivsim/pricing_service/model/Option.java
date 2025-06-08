package com.derivsim.pricing_service.model;

import lombok.*;

public class Option {
    private String symbol;
    private double spotPrice;
    private double strikePrice;
    private double volatility;
    private double riskFreeRate;
    private double timeToMaturity;
    private boolean isCall;

    // Constructor
    public Option(String symbol, double spotPrice, double strikePrice, double volatility,
                  double riskFreeRate, double timeToMaturity, boolean isCall) {
        this.symbol = symbol;
        this.spotPrice = spotPrice;
        this.strikePrice = strikePrice;
        this.volatility = volatility;
        this.riskFreeRate = riskFreeRate;
        this.timeToMaturity = timeToMaturity;
        this.isCall = isCall;
    }

    // Getters
    public String getSymbol() {
        return symbol;
    }

    public double getSpotPrice() {
        return spotPrice;
    }

    public double getStrikePrice() {
        return strikePrice;
    }

    public double getVolatility() {
        return volatility;
    }

    public double getRiskFreeRate() {
        return riskFreeRate;
    }

    public double getTimeToMaturity() {
        return timeToMaturity;
    }

    public boolean isCall() {
        return isCall;
    }
}
