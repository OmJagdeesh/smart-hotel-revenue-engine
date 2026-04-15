package com.staywise.hotel_revenue_engine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class AnalyticsResponse {
    private Long hotelId;
    private BigDecimal averageOccupancy;
    private BigDecimal totalRevenue;
    private BigDecimal averageRevPar;
    private List<AnalyticsPoint> trend;
}
