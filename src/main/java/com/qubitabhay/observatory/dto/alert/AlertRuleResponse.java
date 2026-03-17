package com.qubitabhay.observatory.dto.alert;

public record AlertRuleResponse(
        Long id,
        String metricName,
        String operator,
        Double threshold,
        String severity,
        Long serviceId
) {
}
