package com.derivsim.pricing_service.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteRequest {
    private String symbol;
    private double spotPrice;
    private double strikePrice;
    private double volatility;
    private double riskFreeRate;
    private double timeToMaturity;
    private boolean isCall;
    private String model; // e.g., "BLACK_SCHOLES", "BINOMIAL"

    public String getSymbol() { return symbol; }
    public double getSpotPrice() { return spotPrice; }
    public double getStrikePrice() { return strikePrice; }
    public double getVolatility() { return volatility; }
    public double getRiskFreeRate() { return riskFreeRate; }
    public double getTimeToMaturity() { return timeToMaturity; }
    public boolean isCall() { return isCall; }
    public String getModel() { return model; }

    // Optionally: setters and constructor
}
