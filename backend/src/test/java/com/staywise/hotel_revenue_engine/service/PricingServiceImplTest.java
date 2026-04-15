package com.staywise.hotel_revenue_engine.service;

import com.staywise.hotel_revenue_engine.dto.PricingRequest;
import com.staywise.hotel_revenue_engine.dto.PricingResponse;
import com.staywise.hotel_revenue_engine.entity.Room;
import com.staywise.hotel_revenue_engine.repository.BookingRepository;
import com.staywise.hotel_revenue_engine.repository.HotelRepository;
import com.staywise.hotel_revenue_engine.repository.RevenueRecordRepository;
import com.staywise.hotel_revenue_engine.repository.RoomRepository;
import com.staywise.hotel_revenue_engine.service.impl.PricingServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PricingServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BookingService bookingService;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RevenueRecordRepository revenueRecordRepository;

    private PricingServiceImpl pricingService;

    @BeforeEach
    void setup() {
        pricingService = new PricingServiceImpl(
                roomRepository,
                bookingService,
                hotelRepository,
                bookingRepository,
                revenueRecordRepository
        );
    }

    @Test
    void shouldCalculateDynamicPrice() {
        Room room = new Room();
        room.setId(10L);
        room.setBasePrice(new BigDecimal("1000.00"));

        PricingRequest request = new PricingRequest();
        request.setHotelId(1L);
        request.setRoomId(10L);
        request.setBusinessDate(LocalDate.now());
        request.setDemandFactor(1.2);

        when(roomRepository.findById(10L)).thenReturn(Optional.of(room));
        when(bookingService.getOccupancyRate(1L, request.getBusinessDate())).thenReturn(0.8);

        PricingResponse response = pricingService.calculatePrice(request);

        Assertions.assertEquals(new BigDecimal("1000.00"), response.getBasePrice());
        Assertions.assertEquals(new BigDecimal("0.800"), response.getOccupancyRate());
        Assertions.assertEquals(new BigDecimal("1.200"), response.getDemandFactor());
        Assertions.assertEquals(new BigDecimal("1680.00"), response.getFinalPrice());
    }
}
