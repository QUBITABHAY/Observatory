package com.qubitabhay.observatory.dto.host;

import jakarta.validation.constraints.NotBlank;

public record CreateHostRequest(
        @NotBlank String hostname,
        @NotBlank String ipAddress,
        @NotBlank String environment
) {
}
