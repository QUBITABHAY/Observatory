package com.qubitabhay.observatory.dto.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateServiceRequest(
        @NotBlank String name,
        @NotNull Long hostId
) {
}
