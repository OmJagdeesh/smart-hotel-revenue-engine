package com.staywise.hotel_revenue_engine.service.impl;

import com.staywise.hotel_revenue_engine.dto.AnalyticsPoint;
import com.staywise.hotel_revenue_engine.dto.AnalyticsResponse;
import com.staywise.hotel_revenue_engine.entity.RevenueRecord;
import com.staywise.hotel_revenue_engine.repository.RevenueRecordRepository;
import com.staywise.hotel_revenue_engine.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final RevenueRecordRepository revenueRecordRepository;

    @Override
    public AnalyticsResponse getHotelAnalytics(Long hotelId, LocalDate startDate, LocalDate endDate) {
        List<RevenueRecord> records = revenueRecordRepository
                .findByHotelIdAndBusinessDateBetweenOrderByBusinessDateAsc(hotelId, startDate, endDate);

        BigDecimal totalRevenue = records.stream()
                .map(RevenueRecord::getTotalRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal avgOccupancy = records.isEmpty() ? BigDecimal.ZERO : records.stream()
                .map(RevenueRecord::getOccupancyRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(records.size()), 3, RoundingMode.HALF_UP);

        BigDecimal avgRevPar = records.isEmpty() ? BigDecimal.ZERO : records.stream()
                .map(RevenueRecord::getRevPar)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(records.size()), 2, RoundingMode.HALF_UP);

        List<AnalyticsPoint> trend = records.stream()
                .map(record -> new AnalyticsPoint(
                        record.getBusinessDate(),
                        record.getTotalRevenue(),
                        record.getOccupancyRate(),
                        record.getRevPar()))
                .toList();

        return new AnalyticsResponse(hotelId, avgOccupancy, totalRevenue, avgRevPar, trend);
    }
}
