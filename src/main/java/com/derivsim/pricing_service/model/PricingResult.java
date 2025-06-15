package com.derivsim.pricing_service.model;
import lombok.*;


import java.time.LocalDateTime;
public class PricingResult {
    private String symbol;
    private double price;
    private String model;
    private LocalDateTime timestamp;
    private double delta;
    private double gamma;

    public PricingResult() {}

    public PricingResult(String symbol, double price, String model, LocalDateTime timestamp, double delta, double gamma) {
        this.symbol = symbol;
        this.price = price;
        this.model = model;
        this.timestamp = timestamp;
        this.delta = delta;
        this.gamma = gamma;
    }


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

}
