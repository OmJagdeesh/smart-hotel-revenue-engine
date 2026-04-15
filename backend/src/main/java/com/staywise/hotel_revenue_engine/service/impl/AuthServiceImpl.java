package com.staywise.hotel_revenue_engine.service.impl;

import com.staywise.hotel_revenue_engine.dto.AuthResponse;
import com.staywise.hotel_revenue_engine.dto.LoginRequest;
import com.staywise.hotel_revenue_engine.dto.RegisterRequest;
import com.staywise.hotel_revenue_engine.entity.Hotel;
import com.staywise.hotel_revenue_engine.entity.Role;
import com.staywise.hotel_revenue_engine.entity.User;
import com.staywise.hotel_revenue_engine.exception.BusinessException;
import com.staywise.hotel_revenue_engine.exception.ResourceNotFoundException;
import com.staywise.hotel_revenue_engine.repository.HotelRepository;
import com.staywise.hotel_revenue_engine.repository.UserRepository;
import com.staywise.hotel_revenue_engine.security.JwtService;
import com.staywise.hotel_revenue_engine.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new BusinessException("Email already registered.");
        });

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole(request.getRole());

        if (request.getRole() != Role.ADMIN) {
            if (request.getHotelId() == null) {
                throw new BusinessException("hotelId is required for hotel users.");
            }
            Hotel hotel = hotelRepository.findById(request.getHotelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Hotel not found."));
            user.setHotel(hotel);
        }

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser);

        return new AuthResponse(
                token,
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getHotel() != null ? savedUser.getHotel().getId() : null
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getHotel() != null ? user.getHotel().getId() : null
        );
    }
}
