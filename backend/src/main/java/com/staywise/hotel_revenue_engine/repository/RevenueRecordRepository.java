package com.staywise.hotel_revenue_engine.repository;

import com.staywise.hotel_revenue_engine.entity.RevenueRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RevenueRecordRepository extends JpaRepository<RevenueRecord, Long> {
    Optional<RevenueRecord> findByHotelIdAndBusinessDate(Long hotelId, LocalDate businessDate);
    List<RevenueRecord> findByHotelIdAndBusinessDateBetweenOrderByBusinessDateAsc(Long hotelId, LocalDate startDate, LocalDate endDate);
}
