package com.qubitabhay.observatory.dto.log;

import java.time.LocalDateTime;

public record LogResponse(
        Long id,
        String level,
        String message,
        String traceId,
        Long hostId,
        Long serviceId,
        LocalDateTime timestamp
) {}
