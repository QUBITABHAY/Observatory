package com.qubitabhay.observatory.dto.user;

import com.qubitabhay.observatory.model.UserRole;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        UserRole role,
        LocalDateTime createdAt
) {
}
