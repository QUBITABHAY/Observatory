package com.qubitabhay.observatory.dto.service;

import java.time.LocalDateTime;

public record ServiceResponse(
        Long id,
        String name,
        Long hostId,
        LocalDateTime createdAt
) {
}
