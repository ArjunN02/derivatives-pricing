package com.derivsim.pricing_service.config;

import com.derivsim.pricing_service.model.PricingResult;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, PricingResult> pricingResultConsumerFactory() {
        JsonDeserializer<PricingResult> deserializer = new JsonDeserializer<>(PricingResult.class, false);
        deserializer.addTrustedPackages("*"); // allow all package sources

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "hedging-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                deserializer
        );
    }


    @Bean(name = "pricingKafkaListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, PricingResult> pricingKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PricingResult> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(pricingResultConsumerFactory());  // use your existing consumerFactory() method
        return factory;
    }

}
