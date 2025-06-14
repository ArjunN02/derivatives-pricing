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

    // Getters, setters, constructor(s)
    // Required by JPA
    public PricingResultEntity() {}

    public PricingResultEntity(String symbol, double price, String model, LocalDateTime timestamp) {
        this.symbol = symbol;
        this.price = price;
        this.model = model;
        this.timestamp = timestamp;
    }

    // Getters and Setters here
}
