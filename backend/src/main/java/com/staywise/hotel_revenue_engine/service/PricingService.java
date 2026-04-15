package com.staywise.hotel_revenue_engine.service;

import com.staywise.hotel_revenue_engine.dto.PricingRequest;
import com.staywise.hotel_revenue_engine.dto.PricingResponse;

public interface PricingService {
    PricingResponse calculatePrice(PricingRequest request);
    void refreshDailyRevenueMetrics();
}
