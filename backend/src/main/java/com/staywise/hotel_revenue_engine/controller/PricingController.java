package com.staywise.hotel_revenue_engine.controller;

import com.staywise.hotel_revenue_engine.dto.PricingRequest;
import com.staywise.hotel_revenue_engine.dto.PricingResponse;
import com.staywise.hotel_revenue_engine.security.TenantAccessValidator;
import com.staywise.hotel_revenue_engine.service.PricingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pricing")
@RequiredArgsConstructor
public class PricingController {

    private final PricingService pricingService;
    private final TenantAccessValidator tenantAccessValidator;

    @PostMapping("/calculate")
    public ResponseEntity<PricingResponse> calculatePrice(@Valid @RequestBody PricingRequest request) {
        tenantAccessValidator.validateHotelAccess(request.getHotelId());
        return ResponseEntity.ok(pricingService.calculatePrice(request));
    }
}
