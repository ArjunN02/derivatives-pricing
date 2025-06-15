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

        String action;

        if (result.getModel().equals("BinomialTree") && result.getDelta() == 0.0) {
            action = (result.getPrice() > 100) ? "SELL STOCK" : "BUY STOCK";
        } else {
            if (Math.abs(result.getDelta()) < 0.2) {
                action = "HOLD";
            } else if (result.getDelta() > 0.2) {
                action = "BUY STOCK";
            } else {
                action = "SELL STOCK";
            }
        }

        HedgingActionEntity hedge = new HedgingActionEntity(
                result.getSymbol(),
                action,
                result.getPrice(),
                result.getModel(),
                LocalDateTime.now()
        );

        hedgingRepo.save(hedge);
        System.out.println("✅ SAVED TO hedging_log: " + result.getSymbol());

    }
}
