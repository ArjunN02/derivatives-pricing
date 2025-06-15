package com.derivsim.pricing_service.controller;

import com.derivsim.pricing_service.entity.PricingResultEntity;
import com.derivsim.pricing_service.model.Option;
import com.derivsim.pricing_service.model.QuoteRequest;
import com.derivsim.pricing_service.model.PricingResult;
import com.derivsim.pricing_service.repository.PricingResultRepository;
import com.derivsim.pricing_service.service.BlackScholesService;
import com.derivsim.pricing_service.service.KafkaProducerService;
import com.derivsim.pricing_service.service.BinomialTreeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

    @Autowired
    private BlackScholesService blackScholesService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private PricingResultRepository pricingResultRepository;

    @Autowired
    private BinomialTreeService binomialTreeService;

    @PostMapping
    public PricingResult getPrice(@RequestBody QuoteRequest req) {
        // 1. Build option from request
        Option option = new Option(
                req.getSymbol(),
                req.getSpotPrice(),
                req.getStrikePrice(),
                req.getVolatility(),
                req.getRiskFreeRate(),
                req.getTimeToMaturity(),
                req.isCall()
        );

        // 2. Compute result using selected model
        PricingResult result;
        switch (req.getModel()) {
            case "BlackScholes":
            default:
                result = blackScholesService.calculateWithGreeks(option);
                break;
            case "BinomialTree":
                double binomialPrice = binomialTreeService.calculate(option).getPrice();
                result = new PricingResult(
                        req.getSymbol(),
                        binomialPrice,
                        req.getModel(),
                        LocalDateTime.now(),
                        0.0, // delta
                        0.0  // gamma
                );
                break;


        }

        // 3. Send result to Kafka
        kafkaProducerService.send(result);

        // 4. Save result to PostgreSQL
        PricingResultEntity entity = new PricingResultEntity(
                result.getSymbol(),
                result.getPrice(),
                result.getModel(),
                result.getTimestamp()
        );

        return result;
    }
}
