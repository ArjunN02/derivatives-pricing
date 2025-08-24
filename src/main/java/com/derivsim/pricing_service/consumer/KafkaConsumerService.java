package com.derivsim.pricing_service.consumer;

import com.derivsim.pricing_service.entity.PricingResultEntity;
import com.derivsim.pricing_service.model.PricingResult;
import com.derivsim.pricing_service.repository.PricingResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    private PricingResultRepository repository;

    @KafkaListener(
            topics = "pricing-results-topic",
            groupId = "pricing-consumer-group",
            containerFactory = "pricingKafkaListenerFactory"
    )
    public void consume(PricingResult result) {
        System.out.println("RECEIVED FROM KAFKA: " + result);
    }
}
