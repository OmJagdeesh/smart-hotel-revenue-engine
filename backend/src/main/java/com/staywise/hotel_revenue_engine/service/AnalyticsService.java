package com.staywise.hotel_revenue_engine.service;

import com.staywise.hotel_revenue_engine.dto.AnalyticsResponse;

import java.time.LocalDate;

public interface AnalyticsService {
    AnalyticsResponse getHotelAnalytics(Long hotelId, LocalDate startDate, LocalDate endDate);
}
