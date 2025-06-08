package com.derivsim.pricing_service.service;

import com.derivsim.pricing_service.model.PricingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, PricingResult> kafkaTemplate;

    private static final String TOPIC = "pricing-results-topic";

    public void send(PricingResult result) {
        kafkaTemplate.send(TOPIC, result);
    }
}
