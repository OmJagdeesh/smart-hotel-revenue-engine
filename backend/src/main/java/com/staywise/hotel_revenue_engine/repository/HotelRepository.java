package com.staywise.hotel_revenue_engine.repository;

import com.staywise.hotel_revenue_engine.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Optional<Hotel> findByName(String name);
}
