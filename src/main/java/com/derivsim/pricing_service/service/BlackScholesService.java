package com.derivsim.pricing_service.service;

import com.derivsim.pricing_service.model.Option;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.springframework.stereotype.Service;

@Service
public class BlackScholesService {

    public double calculate(Option option) {
        double S = option.getSpotPrice();
        double K = option.getStrikePrice();
        double T = option.getTimeToMaturity();
        double r = option.getRiskFreeRate();
        double sigma = option.getVolatility();
        boolean isCall = option.isCall();

        double d1 = (Math.log(S / K) + (r + (sigma * sigma) / 2.0) * T) / (sigma * Math.sqrt(T));
        double d2 = d1 - sigma * Math.sqrt(T);

        NormalDistribution nd = new NormalDistribution();
        if (isCall) {
            return S * nd.cumulativeProbability(d1) - K * Math.exp(-r * T) * nd.cumulativeProbability(d2);
        } else {
            return K * Math.exp(-r * T) * nd.cumulativeProbability(-d2) - S * nd.cumulativeProbability(-d1);
        }
    }
}
