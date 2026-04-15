package com.staywise.hotel_revenue_engine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PricingResponse {
    private Long roomId;
    private BigDecimal basePrice;
    private BigDecimal occupancyRate;
    private BigDecimal demandFactor;
    private BigDecimal finalPrice;
}
