package com.staywise.hotel_revenue_engine.repository;

import com.staywise.hotel_revenue_engine.entity.PricingRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PricingRuleRepository extends JpaRepository<PricingRule, Long> {
    List<PricingRule> findByHotelIdAndActiveTrue(Long hotelId);

    List<PricingRule> findByHotelIdAndActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long hotelId,
            LocalDate currentDate,
            LocalDate currentDateAgain
    );
}
