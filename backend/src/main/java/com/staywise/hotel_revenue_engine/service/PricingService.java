package com.staywise.hotel_revenue_engine.service;

import com.staywise.hotel_revenue_engine.dto.PricingRequest;
import com.staywise.hotel_revenue_engine.dto.PricingResponse;
import com.staywise.hotel_revenue_engine.entity.Booking;
import com.staywise.hotel_revenue_engine.entity.BookingStatus;
import com.staywise.hotel_revenue_engine.entity.RevenueRecord;
import com.staywise.hotel_revenue_engine.entity.Room;
import com.staywise.hotel_revenue_engine.exception.ResourceNotFoundException;
import com.staywise.hotel_revenue_engine.repository.BookingRepository;
import com.staywise.hotel_revenue_engine.repository.HotelRepository;
import com.staywise.hotel_revenue_engine.repository.RevenueRecordRepository;
import com.staywise.hotel_revenue_engine.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;
    private final RevenueRecordRepository revenueRecordRepository;

    public PricingResponse calculatePrice(PricingRequest request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found."));

        double occupancyRateValue = getOccupancyRate(request.getHotelId(), request.getBusinessDate());
        BigDecimal occupancyRate = BigDecimal.valueOf(occupancyRateValue).setScale(3, RoundingMode.HALF_UP);
        BigDecimal demandFactor = BigDecimal.valueOf(request.getDemandFactor() == null ? 1.0 : request.getDemandFactor())
                .setScale(3, RoundingMode.HALF_UP);

        BigDecimal basePrice = room.getBasePrice();

        // Formula I used for the mini project:
        // price = base price * demand factor * (1 + occupancy * 0.5)
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

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void refreshDailyRevenueMetrics() {
        LocalDate businessDate = LocalDate.now();

        hotelRepository.findAll().forEach(hotel -> {
            long availableRooms = roomRepository.countByHotelIdAndActiveTrue(hotel.getId());
            long occupiedRooms = Math.round(getOccupancyRate(hotel.getId(), businessDate) * availableRooms);

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
    }

    private double getOccupancyRate(Long hotelId, LocalDate businessDate) {
        long occupiedRooms = bookingRepository.countOccupiedRoomsByHotelAndDate(hotelId, businessDate, BookingStatus.CONFIRMED);
        long totalRooms = roomRepository.countByHotelIdAndActiveTrue(hotelId);

        if (totalRooms == 0) {
            return 0;
        }
        return (double) occupiedRooms / totalRooms;
    }
}
