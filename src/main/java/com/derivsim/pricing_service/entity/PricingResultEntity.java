package com.derivsim.pricing_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pricing_results")
public class PricingResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private double price;
    private String model;
    private LocalDateTime timestamp;

    // new fields to capture request params
    private double spotPrice;
    private double strikePrice;
    private double volatility;
    private double riskFreeRate;
    private double timeToMaturity;
    private boolean isCall;

    public PricingResultEntity() {}

    public PricingResultEntity(String symbol, double price, String model, LocalDateTime timestamp,
                               double spotPrice, double strikePrice, double volatility,
                               double riskFreeRate, double timeToMaturity, boolean isCall) {
        this.symbol = symbol;
        this.price = price;
        this.model = model;
        this.timestamp = timestamp;
        this.spotPrice = spotPrice;
        this.strikePrice = strikePrice;
        this.volatility = volatility;
        this.riskFreeRate = riskFreeRate;
        this.timeToMaturity = timeToMaturity;
        this.isCall = isCall;
    }

    // getters & setters
}
