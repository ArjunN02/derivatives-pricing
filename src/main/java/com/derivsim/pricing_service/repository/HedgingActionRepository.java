package com.derivsim.pricing_service.repository;

import com.derivsim.pricing_service.entity.HedgingActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HedgingActionRepository extends JpaRepository<HedgingActionEntity, Long> {
    List<HedgingActionEntity> findBySymbolOrderByTimestampDesc(String symbol);
    List<HedgingActionEntity> findByModelOrderByTimestampDesc(String model);
    List<HedgingActionEntity> findBySymbolAndModelOrderByTimestampDesc(String symbol, String model);
    List<HedgingActionEntity> findTop50ByOrderByTimestampDesc();
    List<HedgingActionEntity> findTop1BySymbolAndModelOrderByTimestampDesc(String symbol, String model);
}