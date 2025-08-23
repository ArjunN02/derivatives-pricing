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

    public HedgingActionEntity() {
    }

    public HedgingActionEntity(String symbol, String action, double price, String model, LocalDateTime timestamp) {
        this.symbol = symbol;
        this.action = action;
        this.price = price;
        this.model = model;
        this.timestamp = timestamp;
    }

    // Getters & setters (generate via Lombok or manually)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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
}