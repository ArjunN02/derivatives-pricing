package com.derivsim.pricing_service.kafka;
import com.derivsim.pricing_service.model.PricingResult;
import com.derivsim.pricing_service.service.HedgingEngineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaHedgingConsumer {

    @Autowired
    private HedgingEngineService hedgingEngineService;

    private final ObjectMapper objectMapper;

    public KafkaHedgingConsumer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // only inside this class
    }

    @KafkaListener(topics = "pricing-results-topic", groupId = "hedging-group")
    public void consume(String message) {
        try {
            PricingResult result = objectMapper.readValue(message, PricingResult.class);
            System.out.println("MANUALLY DESERIALIZED: " + result);
            hedgingEngineService.handlePricingResult(result);
        } catch (Exception e) {
            System.err.println("Failed to deserialize message: " + e.getMessage());
        }
    }
}
