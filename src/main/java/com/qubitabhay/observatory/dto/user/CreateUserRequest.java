package com.qubitabhay.observatory.dto.user;

import com.qubitabhay.observatory.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank String username,
        @NotBlank @Size(min = 8, message = "Password must be at least 8 characters") String password,
        @NotNull UserRole role
) {
}
