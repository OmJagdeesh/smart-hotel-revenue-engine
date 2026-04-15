package com.staywise.hotel_revenue_engine.service;

import com.staywise.hotel_revenue_engine.dto.AuthResponse;
import com.staywise.hotel_revenue_engine.dto.LoginRequest;
import com.staywise.hotel_revenue_engine.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
