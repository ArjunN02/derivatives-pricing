package com.derivsim.pricing_service.repository;


import com.derivsim.pricing_service.entity.PricingResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PricingResultRepository extends JpaRepository<PricingResultEntity, Long> {
}
