package com.staywise.hotel_revenue_engine.controller;

import com.staywise.hotel_revenue_engine.dto.AnalyticsResponse;
import com.staywise.hotel_revenue_engine.security.TenantAccessValidator;
import com.staywise.hotel_revenue_engine.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final TenantAccessValidator tenantAccessValidator;

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<AnalyticsResponse> getAnalytics(
            @PathVariable Long hotelId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        tenantAccessValidator.validateHotelAccess(hotelId);
        return ResponseEntity.ok(analyticsService.getHotelAnalytics(hotelId, startDate, endDate));
    }
}
