package com.staywise.hotel_revenue_engine.controller;

import com.staywise.hotel_revenue_engine.dto.BookingRequest;
import com.staywise.hotel_revenue_engine.dto.BookingResponse;
import com.staywise.hotel_revenue_engine.security.TenantAccessValidator;
import com.staywise.hotel_revenue_engine.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final TenantAccessValidator tenantAccessValidator;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        tenantAccessValidator.validateHotelAccess(request.getHotelId());
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<BookingResponse>> getHotelBookings(@PathVariable Long hotelId) {
        tenantAccessValidator.validateHotelAccess(hotelId);
        return ResponseEntity.ok(bookingService.getBookingsByHotel(hotelId));
    }

    @GetMapping("/hotel/{hotelId}/occupancy")
    public ResponseEntity<Map<String, Object>> getOccupancy(@PathVariable Long hotelId) {
        tenantAccessValidator.validateHotelAccess(hotelId);
        double occupancyRate = bookingService.getOccupancyRate(hotelId, LocalDate.now());
        Map<String, Object> response = new HashMap<>();
        response.put("hotelId", hotelId);
        response.put("occupancyRate", occupancyRate);
        return ResponseEntity.ok(response);
    }
}
