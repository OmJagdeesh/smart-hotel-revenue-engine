package com.staywise.hotel_revenue_engine.repository;

import com.staywise.hotel_revenue_engine.entity.Booking;
import com.staywise.hotel_revenue_engine.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
            select case when count(b) > 0 then true else false end
            from Booking b
            where b.room.id = :roomId
              and b.status = :status
              and b.checkInDate < :checkOutDate
              and b.checkOutDate > :checkInDate
            """)
    boolean existsOverlap(
            @Param("roomId") Long roomId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("status") BookingStatus status
    );

    @Query("""
            select count(distinct b.room.id)
            from Booking b
            where b.hotel.id = :hotelId
              and b.status = :status
              and b.checkInDate <= :businessDate
              and b.checkOutDate > :businessDate
            """)
    long countOccupiedRoomsByHotelAndDate(
            @Param("hotelId") Long hotelId,
            @Param("businessDate") LocalDate businessDate,
            @Param("status") BookingStatus status
    );

    @Query("""
            select b from Booking b
            join fetch b.hotel h
            join fetch b.room r
            where h.id = :hotelId
            order by b.createdAt desc
            """)
    List<Booking> findByHotelIdOrderByCreatedAtDesc(@Param("hotelId") Long hotelId);
}
