package com.staywise.hotel_revenue_engine.service;

import com.staywise.hotel_revenue_engine.dto.BookingRequest;
import com.staywise.hotel_revenue_engine.dto.BookingResponse;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request);
    List<BookingResponse> getBookingsByHotel(Long hotelId);
    double getOccupancyRate(Long hotelId, LocalDate businessDate);
}
