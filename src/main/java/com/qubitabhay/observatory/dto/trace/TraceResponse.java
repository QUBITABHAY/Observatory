package com.qubitabhay.observatory.dto.trace;

import java.time.LocalDateTime;

public record TraceResponse(
        Long id,
        String traceId,
        Long duration,
        Long serviceId,
        LocalDateTime startedAt
) {}
