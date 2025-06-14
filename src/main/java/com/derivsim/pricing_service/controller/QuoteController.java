package com.derivsim.pricing_service.controller;

import com.derivsim.pricing_service.entity.PricingResultEntity;
import com.derivsim.pricing_service.model.Option;
import com.derivsim.pricing_service.model.QuoteRequest;
import com.derivsim.pricing_service.model.PricingResult;
import com.derivsim.pricing_service.repository.PricingResultRepository;
import com.derivsim.pricing_service.service.BlackScholesService;
import com.derivsim.pricing_service.service.KafkaProducerService;

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

        // 2. Compute price using selected model
        double price;
        switch (req.getModel()) {
            case "BlackScholes":
            default:
                price = blackScholesService.calculate(option);
                break;
        }

        // 3. Build result
        PricingResult result = new PricingResult(
                req.getSymbol(),
                price,
                req.getModel(),
                LocalDateTime.now()
        );

        // 4. Send result to Kafka
        kafkaProducerService.send(result);

        // 5. Save result to PostgreSQL
        PricingResultEntity entity = new PricingResultEntity(
                result.getSymbol(),
                result.getPrice(),
                result.getModel(),
                result.getTimestamp()
        );
        pricingResultRepository.save(entity);

        // 6. Return response
        return result;
    }
}
