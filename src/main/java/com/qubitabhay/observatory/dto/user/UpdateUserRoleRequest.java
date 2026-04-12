package com.qubitabhay.observatory.dto.user;

import com.qubitabhay.observatory.model.UserRole;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRoleRequest(
        @NotNull UserRole role
) {
}
