package com.derivsim.pricing_service.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteRequest {
    private @Getter @Setter String symbol;
    private @Getter @Setter double spotPrice;
    private @Getter @Setter double strikePrice;
    private @Getter @Setter double volatility;
    private @Getter @Setter double riskFreeRate;
    private @Getter @Setter double timeToMaturity;
    
    @JsonProperty("isCall")
    private @Getter @Setter boolean call;
    private @Getter @Setter String model;
}
