package com.derivsim.pricing_service.model;
import lombok.*;


import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class PricingResult {
    public PricingResult(String symbol, double price, String model, LocalDateTime timestamp) {
        this.symbol = symbol;
        this.optionPrice = price;
        this.modelUsed = model;
        this.timestamp = timestamp;
    }
    public PricingResult() {
        // Default constructor for Jackson
    }

    private String symbol;
    private double optionPrice;
    private String modelUsed;
    private LocalDateTime timestamp;

    // Getters
    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return optionPrice;
    }

    public String getModel() {
        return modelUsed;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
