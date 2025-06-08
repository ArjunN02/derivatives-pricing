package com.derivsim.pricing_service.controller;

import com.derivsim.pricing_service.model.Option;
import com.derivsim.pricing_service.model.QuoteRequest;
import com.derivsim.pricing_service.model.PricingResult;
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

    @PostMapping
    public PricingResult getPrice(@RequestBody QuoteRequest req) {
        Option option = new Option(
                req.getSymbol(),
                req.getSpotPrice(),
                req.getStrikePrice(),
                req.getVolatility(),
                req.getRiskFreeRate(),
                req.getTimeToMaturity(),
                req.isCall()
        );

        double price;
        switch (req.getModel()) {
            case "BlackScholes":
            default:
                price = blackScholesService.calculate(option);
                break;
        }

        PricingResult result = new PricingResult(
                req.getSymbol(),
                price,
                req.getModel(),
                LocalDateTime.now()
        );

        kafkaProducerService.send(result);  // send to Kafka

        return result;
    }
}
