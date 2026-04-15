package com.staywise.hotel_revenue_engine.service;

import com.staywise.hotel_revenue_engine.dto.BookingRequest;
import com.staywise.hotel_revenue_engine.entity.BookingStatus;
import com.staywise.hotel_revenue_engine.entity.Hotel;
import com.staywise.hotel_revenue_engine.entity.Role;
import com.staywise.hotel_revenue_engine.entity.Room;
import com.staywise.hotel_revenue_engine.entity.User;
import com.staywise.hotel_revenue_engine.exception.BusinessException;
import com.staywise.hotel_revenue_engine.repository.BookingRepository;
import com.staywise.hotel_revenue_engine.repository.HotelRepository;
import com.staywise.hotel_revenue_engine.repository.RoomRepository;
import com.staywise.hotel_revenue_engine.repository.UserRepository;
import com.staywise.hotel_revenue_engine.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private PricingService pricingService;

    private BookingServiceImpl bookingService;

    @BeforeEach
    void setup() {
        bookingService = new BookingServiceImpl(
                bookingRepository,
                roomRepository,
                userRepository,
                hotelRepository,
                pricingService
        );
    }

    @Test
    void shouldRejectDoubleBooking() {
        BookingRequest request = new BookingRequest();
        request.setHotelId(1L);
        request.setRoomId(1L);
        request.setUserId(1L);
        request.setCheckInDate(LocalDate.now().plusDays(1));
        request.setCheckOutDate(LocalDate.now().plusDays(2));

        Hotel hotel = new Hotel();
        hotel.setId(1L);

        Room room = new Room();
        room.setId(1L);
        room.setHotel(hotel);
        room.setBasePrice(new BigDecimal("1000.00"));

        User user = new User();
        user.setId(1L);
        user.setRole(Role.HOTEL_MANAGER);
        user.setHotel(hotel);

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.existsOverlap(any(), any(), any(), any(BookingStatus.class))).thenReturn(true);

        Assertions.assertThrows(BusinessException.class, () -> bookingService.createBooking(request));
    }
}
