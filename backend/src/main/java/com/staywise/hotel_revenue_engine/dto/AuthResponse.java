package com.staywise.hotel_revenue_engine.dto;

import com.staywise.hotel_revenue_engine.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long userId;
    private String email;
    private Role role;
    private Long hotelId;
}
