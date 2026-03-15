package com.qubitabhay.observatory.dto.log;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateLogRequest(
        @NotBlank(message = "level is required") String level,
        @NotBlank(message = "message is required") String message,
        String traceId,
        @NotNull(message = "hostId is required") Long hostId,
        @NotNull(message = "serviceId is required") Long serviceId
) {}
