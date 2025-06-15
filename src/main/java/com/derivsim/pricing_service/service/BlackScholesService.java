// BlackScholesService.java
package com.derivsim.pricing_service.service;

import com.derivsim.pricing_service.model.Option;
import com.derivsim.pricing_service.model.PricingResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BlackScholesService {

    public PricingResult calculateWithGreeks(Option option) {
        double S = option.getSpotPrice();
        double K = option.getStrikePrice();
        double T = option.getTimeToMaturity();
        double r = option.getRiskFreeRate();
        double sigma = option.getVolatility();

        double d1 = (Math.log(S / K) + (r + Math.pow(sigma, 2) / 2) * T) / (sigma * Math.sqrt(T));
        double d2 = d1 - sigma * Math.sqrt(T);

        double price, delta, gamma;

        if (option.isCall()) {
            price = S * normCDF(d1) - K * Math.exp(-r * T) * normCDF(d2);
            delta = normCDF(d1);
        } else {
            price = K * Math.exp(-r * T) * normCDF(-d2) - S * normCDF(-d1);
            delta = -normCDF(-d1);
        }

        gamma = normPDF(d1) / (S * sigma * Math.sqrt(T));

        return new PricingResult(option.getSymbol(), price, "BlackScholes", LocalDateTime.now(), delta, gamma);
    }

    private double normCDF(double x) {
        return 0.5 * (1.0 + erf(x / Math.sqrt(2.0)));
    }

    private double normPDF(double x) {
        return Math.exp(-0.5 * x * x) / Math.sqrt(2 * Math.PI);
    }

    private double erf(double z) {
        double t = 1.0 / (1.0 + 0.5 * Math.abs(z));
        double ans = 1 - t * Math.exp(-z*z   - 1.26551223 + t*( 1.00002368 + t*( 0.37409196 +
                t*( 0.09678418 + t*(-0.18628806 + t*( 0.27886807 +
                        t*(-1.13520398 + t*( 1.48851587 + t*(-0.82215223 +
                                t*0.17087277)))))))));
        return z >= 0 ? ans : -ans;
    }
}
