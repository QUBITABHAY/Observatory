package com.qubitabhay.observatory.dto.span;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateSpanRequest(
        @NotBlank(message = "spanId is required") String spanId,
        String parentSpanId,
        @NotNull(message = "duration is required") @Positive(message = "duration must be positive") Long duration,
        @NotBlank(message = "traceId is required") String traceId,
        @NotNull(message = "serviceId is required") Long serviceId
) {}
