// BinomialTreeService.java
package com.derivsim.pricing_service.service;

import com.derivsim.pricing_service.model.Option;
import com.derivsim.pricing_service.model.PricingResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BinomialTreeService {

    public PricingResult calculate(Option option) {
        double S = option.getSpotPrice();
        double K = option.getStrikePrice();
        double T = option.getTimeToMaturity();
        double r = option.getRiskFreeRate();
        double sigma = option.getVolatility();
        boolean isCall = option.isCall();

        int steps = 100;
        double dt = T / steps;
        double u = Math.exp(sigma * Math.sqrt(dt));
        double d = 1 / u;
        double p = (Math.exp(r * dt) - d) / (u - d);

        double[] prices = new double[steps + 1];
        double[] optionValues = new double[steps + 1];

        // Compute terminal prices and payoffs
        for (int i = 0; i <= steps; i++) {
            prices[i] = S * Math.pow(u, steps - i) * Math.pow(d, i);
            optionValues[i] = isCall ? Math.max(0, prices[i] - K) : Math.max(0, K - prices[i]);
        }

        // Step backwards through the tree
        for (int step = steps - 1; step >= 0; step--) {
            for (int i = 0; i <= step; i++) {
                optionValues[i] = Math.exp(-r * dt) * (p * optionValues[i] + (1 - p) * optionValues[i + 1]);
            }
        }

        return new PricingResult(option.getSymbol(), optionValues[0], "BinomialTree", LocalDateTime.now(), 0.0, 0.0);
    }
}
