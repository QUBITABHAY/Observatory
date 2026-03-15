package com.qubitabhay.observatory.dto.span;

import java.time.LocalDateTime;

public record SpanResponse(
        Long id,
        String spanId,
        String parentSpanId,
        Long duration,
        String traceId,
        Long serviceId,
        LocalDateTime timestamp
) {}
