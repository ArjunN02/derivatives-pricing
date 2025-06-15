package com.derivsim.pricing_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hedging_log")
public class HedgingActionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private String action;
    private double price;
    private String model;
    private LocalDateTime timestamp;

    public HedgingActionEntity() {}

    public HedgingActionEntity(String symbol, String action, double price, String model, LocalDateTime timestamp) {
        this.symbol = symbol;
        this.action = action;
        this.price = price;
        this.model = model;
        this.timestamp = timestamp;
    }

    // Getters & setters (generate via Lombok or manually)
}
