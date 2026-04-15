package com.staywise.hotel_revenue_engine.service.impl;

import com.staywise.hotel_revenue_engine.dto.PricingRequest;
import com.staywise.hotel_revenue_engine.dto.PricingResponse;
import com.staywise.hotel_revenue_engine.entity.Booking;
import com.staywise.hotel_revenue_engine.entity.RevenueRecord;
import com.staywise.hotel_revenue_engine.entity.Room;
import com.staywise.hotel_revenue_engine.exception.ResourceNotFoundException;
import com.staywise.hotel_revenue_engine.repository.BookingRepository;
import com.staywise.hotel_revenue_engine.repository.HotelRepository;
import com.staywise.hotel_revenue_engine.repository.RevenueRecordRepository;
import com.staywise.hotel_revenue_engine.repository.RoomRepository;
import com.staywise.hotel_revenue_engine.service.BookingService;
import com.staywise.hotel_revenue_engine.service.PricingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PricingServiceImpl implements PricingService {

    private final RoomRepository roomRepository;
    private final BookingService bookingService;
    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;
    private final RevenueRecordRepository revenueRecordRepository;

    @Override
    public PricingResponse calculatePrice(PricingRequest request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found."));

        double occupancyRateValue = bookingService.getOccupancyRate(request.getHotelId(), request.getBusinessDate());
        BigDecimal occupancyRate = BigDecimal.valueOf(occupancyRateValue).setScale(3, RoundingMode.HALF_UP);
        BigDecimal demandFactor = BigDecimal.valueOf(request.getDemandFactor() == null ? 1.0 : request.getDemandFactor())
                .setScale(3, RoundingMode.HALF_UP);

        BigDecimal basePrice = room.getBasePrice();

        // Dynamic pricing formula:
        // price = base_price * demand_factor * (1 + occupancy_rate * 0.5)
        BigDecimal occupancyAdjustment = BigDecimal.ONE.add(occupancyRate.multiply(BigDecimal.valueOf(0.5)));
        BigDecimal finalPrice = basePrice.multiply(demandFactor).multiply(occupancyAdjustment)
                .setScale(2, RoundingMode.HALF_UP);

        return new PricingResponse(
                room.getId(),
                basePrice,
                occupancyRate,
                demandFactor,
                finalPrice
        );
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void refreshDailyRevenueMetrics() {
        LocalDate businessDate = LocalDate.now();

        hotelRepository.findAll().forEach(hotel -> {
            long availableRooms = roomRepository.countByHotelIdAndActiveTrue(hotel.getId());
            long occupiedRooms = Math.round(bookingService.getOccupancyRate(hotel.getId(), businessDate) * availableRooms);

            List<Booking> bookings = bookingRepository.findByHotelIdOrderByCreatedAtDesc(hotel.getId());
            BigDecimal totalRevenue = bookings.stream()
                    .filter(booking -> booking.getCheckInDate().equals(businessDate))
                    .map(Booking::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal occupancyRate = availableRooms == 0
                    ? BigDecimal.ZERO
                    : BigDecimal.valueOf((double) occupiedRooms / availableRooms).setScale(3, RoundingMode.HALF_UP);

            BigDecimal revPar = availableRooms == 0
                    ? BigDecimal.ZERO
                    : totalRevenue.divide(BigDecimal.valueOf(availableRooms), 2, RoundingMode.HALF_UP);

            RevenueRecord revenueRecord = revenueRecordRepository
                    .findByHotelIdAndBusinessDate(hotel.getId(), businessDate)
                    .orElseGet(RevenueRecord::new);

            revenueRecord.setHotel(hotel);
            revenueRecord.setBusinessDate(businessDate);
            revenueRecord.setTotalRevenue(totalRevenue);
            revenueRecord.setOccupiedRooms((int) occupiedRooms);
            revenueRecord.setAvailableRooms((int) availableRooms);
            revenueRecord.setOccupancyRate(occupancyRate);
            revenueRecord.setRevPar(revPar);
            revenueRecordRepository.save(revenueRecord);
        });

        log.info("Daily revenue metrics refreshed for {}", businessDate);
    }
}
