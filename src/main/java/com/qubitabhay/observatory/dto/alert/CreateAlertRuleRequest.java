package com.qubitabhay.observatory.dto.alert;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateAlertRuleRequest(
        @NotBlank(message = "metricName is required") String metricName,
        @NotBlank(message = "operator is required") String operator,
        @NotNull(message = "threshold is required") @PositiveOrZero(message = "threshold must be zero or positive") Double threshold,
        @NotBlank(message = "severity is required") String severity,
        @NotNull(message = "serviceId is required") Long serviceId
) {
}
