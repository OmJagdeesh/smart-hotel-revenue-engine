package com.staywise.hotel_revenue_engine.security;

import com.staywise.hotel_revenue_engine.entity.Role;
import com.staywise.hotel_revenue_engine.entity.User;
import com.staywise.hotel_revenue_engine.exception.BusinessException;
import com.staywise.hotel_revenue_engine.exception.ResourceNotFoundException;
import com.staywise.hotel_revenue_engine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TenantAccessValidator {

    private final UserRepository userRepository;

    public void validateHotelAccess(Long hotelId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("Unauthenticated request.");
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ROLE_" + Role.ADMIN.name()));

        if (isAdmin) {
            return;
        }

        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found."));

        Long userHotelId = currentUser.getHotel() != null ? currentUser.getHotel().getId() : null;
        if (userHotelId == null || !userHotelId.equals(hotelId)) {
            throw new BusinessException("Access denied for this tenant.");
        }
    }
}
