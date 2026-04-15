package com.staywise.hotel_revenue_engine.service.impl;

import com.staywise.hotel_revenue_engine.dto.BookingRequest;
import com.staywise.hotel_revenue_engine.dto.BookingResponse;
import com.staywise.hotel_revenue_engine.dto.PricingRequest;
import com.staywise.hotel_revenue_engine.dto.PricingResponse;
import com.staywise.hotel_revenue_engine.entity.Booking;
import com.staywise.hotel_revenue_engine.entity.BookingStatus;
import com.staywise.hotel_revenue_engine.entity.Hotel;
import com.staywise.hotel_revenue_engine.entity.Room;
import com.staywise.hotel_revenue_engine.entity.User;
import com.staywise.hotel_revenue_engine.exception.BusinessException;
import com.staywise.hotel_revenue_engine.exception.ResourceNotFoundException;
import com.staywise.hotel_revenue_engine.repository.BookingRepository;
import com.staywise.hotel_revenue_engine.repository.HotelRepository;
import com.staywise.hotel_revenue_engine.repository.RoomRepository;
import com.staywise.hotel_revenue_engine.repository.UserRepository;
import com.staywise.hotel_revenue_engine.service.BookingService;
import com.staywise.hotel_revenue_engine.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final PricingService pricingService;

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        if (!request.getCheckOutDate().isAfter(request.getCheckInDate())) {
            throw new BusinessException("checkOutDate must be after checkInDate.");
        }

        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found."));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found."));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (!room.getHotel().getId().equals(hotel.getId())) {
            throw new BusinessException("Room does not belong to hotel.");
        }
        if (user.getHotel() != null && !user.getHotel().getId().equals(hotel.getId())) {
            throw new BusinessException("User does not belong to hotel.");
        }

        boolean roomBusy = bookingRepository.existsOverlap(
                request.getRoomId(),
                request.getCheckInDate(),
                request.getCheckOutDate(),
                BookingStatus.CONFIRMED
        );

        if (roomBusy) {
            throw new BusinessException("Room is already booked for the selected date range.");
        }

        PricingRequest pricingRequest = new PricingRequest();
        pricingRequest.setHotelId(request.getHotelId());
        pricingRequest.setRoomId(request.getRoomId());
        pricingRequest.setBusinessDate(request.getCheckInDate());
        pricingRequest.setDemandFactor(request.getDemandFactor());
        PricingResponse pricingResponse = pricingService.calculatePrice(pricingRequest);

        long nights = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        BigDecimal totalPrice = pricingResponse.getFinalPrice().multiply(BigDecimal.valueOf(nights));

        Booking booking = new Booking();
        booking.setHotel(hotel);
        booking.setRoom(room);
        booking.setUser(user);
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.CONFIRMED);

        Booking savedBooking = bookingRepository.save(booking);

        return new BookingResponse(
                savedBooking.getId(),
                hotel.getId(),
                room.getId(),
                savedBooking.getCheckInDate(),
                savedBooking.getCheckOutDate(),
                savedBooking.getTotalPrice(),
                savedBooking.getStatus()
        );
    }

    @Override
    public List<BookingResponse> getBookingsByHotel(Long hotelId) {
        return bookingRepository.findByHotelIdOrderByCreatedAtDesc(hotelId)
                .stream()
                .map(booking -> new BookingResponse(
                        booking.getId(),
                        booking.getHotel().getId(),
                        booking.getRoom().getId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(),
                        booking.getTotalPrice(),
                        booking.getStatus()
                ))
                .toList();
    }

    @Override
    public double getOccupancyRate(Long hotelId, LocalDate businessDate) {
        long occupiedRooms = bookingRepository.countOccupiedRoomsByHotelAndDate(hotelId, businessDate, BookingStatus.CONFIRMED);
        long totalRooms = roomRepository.countByHotelIdAndActiveTrue(hotelId);

        if (totalRooms == 0) {
            return 0;
        }
        return (double) occupiedRooms / totalRooms;
    }
}
