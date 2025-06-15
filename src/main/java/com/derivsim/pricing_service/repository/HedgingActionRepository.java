package com.derivsim.pricing_service.repository;

import com.derivsim.pricing_service.entity.HedgingActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HedgingActionRepository extends JpaRepository<HedgingActionEntity, Long> {
}
