package com.qubitabhay.observatory.dto.trace;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateTraceRequest(
        @NotBlank(message = "traceId is required") String traceId,
        @NotNull(message = "duration is required") @Positive(message = "duration must be positive") Long duration,
        @NotNull(message = "serviceId is required") Long serviceId
) {}
