package com.staywise.hotel_revenue_engine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class AnalyticsPoint {
    private LocalDate businessDate;
    private BigDecimal totalRevenue;
    private BigDecimal occupancyRate;
    private BigDecimal revPar;
}
