package com.staywise.hotel_revenue_engine.repository;

import com.staywise.hotel_revenue_engine.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelIdAndActiveTrue(Long hotelId);
    long countByHotelIdAndActiveTrue(Long hotelId);
}
