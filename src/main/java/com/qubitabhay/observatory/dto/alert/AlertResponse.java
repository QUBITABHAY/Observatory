package com.qubitabhay.observatory.dto.alert;

import java.time.LocalDateTime;

public record AlertResponse(
        Long id,
        String message,
        String severity,
        boolean resolved,
        boolean silenced,
        LocalDateTime triggeredAt,
        Long serviceId
) {
}
