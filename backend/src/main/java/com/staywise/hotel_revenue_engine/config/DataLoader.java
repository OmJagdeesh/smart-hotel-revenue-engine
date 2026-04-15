package com.staywise.hotel_revenue_engine.config;

import com.staywise.hotel_revenue_engine.entity.Hotel;
import com.staywise.hotel_revenue_engine.entity.Role;
import com.staywise.hotel_revenue_engine.entity.Room;
import com.staywise.hotel_revenue_engine.entity.User;
import com.staywise.hotel_revenue_engine.repository.HotelRepository;
import com.staywise.hotel_revenue_engine.repository.RoomRepository;
import com.staywise.hotel_revenue_engine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (hotelRepository.count() > 0) {
            return;
        }

        Hotel hotel = new Hotel();
        hotel.setName("Sunrise Suites");
        hotel.setLocation("Goa");
        Hotel savedHotel = hotelRepository.save(hotel);

        User admin = new User();
        admin.setEmail("admin@staywise.io");
        admin.setFullName("Platform Admin");
        admin.setPassword(passwordEncoder.encode("Admin@123"));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        User manager = new User();
        manager.setEmail("manager@sunrise.com");
        manager.setFullName("Sunrise Manager");
        manager.setPassword(passwordEncoder.encode("Manager@123"));
        manager.setRole(Role.HOTEL_MANAGER);
        manager.setHotel(savedHotel);
        userRepository.save(manager);

        Room deluxe = new Room();
        deluxe.setHotel(savedHotel);
        deluxe.setRoomNumber("101");
        deluxe.setName("Deluxe Sea View");
        deluxe.setRoomType("DELUXE");
        deluxe.setBasePrice(new BigDecimal("6000.00"));

        Room suite = new Room();
        suite.setHotel(savedHotel);
        suite.setRoomNumber("102");
        suite.setName("Executive Suite");
        suite.setRoomType("SUITE");
        suite.setBasePrice(new BigDecimal("9500.00"));

        roomRepository.save(deluxe);
        roomRepository.save(suite);
    }
}
