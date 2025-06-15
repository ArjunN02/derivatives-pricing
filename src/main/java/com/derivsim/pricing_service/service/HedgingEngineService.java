package com.derivsim.pricing_service.service;

import com.derivsim.pricing_service.model.PricingResult;
import com.derivsim.pricing_service.entity.HedgingActionEntity;
import com.derivsim.pricing_service.repository.HedgingActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HedgingEngineService {

    @Autowired
    private HedgingActionRepository hedgingRepo;

    public void handlePricingResult(PricingResult result) {
        System.out.println("🔥 HEDGING ENGINE PROCESSING: " + result.getSymbol()
                + ", Price: " + result.getPrice()
                + ", Model: " + result.getModel()
                + ", Timestamp: " + result.getTimestamp());

        // Simple delta hedge logic

        String action = (result.getPrice() > 100) ? "SELL STOCK" : "BUY STOCK";

        HedgingActionEntity hedge = new HedgingActionEntity(
                result.getSymbol(),
                action,
                result.getPrice(),
                result.getModel(),
                LocalDateTime.now()
        );

        hedgingRepo.save(hedge);
    }
}
