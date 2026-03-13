package com.qubitabhay.observatory.dto.host;

import java.time.LocalDateTime;

public record HostResponse(
        Long id,
        String hostname,
        String ipAddress,
        String environment,
        LocalDateTime createdAt
) {
}
