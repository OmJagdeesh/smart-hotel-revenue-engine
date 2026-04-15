package com.staywise.hotel_revenue_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HotelRevenueEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelRevenueEngineApplication.class, args);
    }
}
