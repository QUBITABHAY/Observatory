package com.qubitabhay.observatory.service;

import com.qubitabhay.observatory.dto.alert.AlertResponse;
import com.qubitabhay.observatory.model.Alert;
import com.qubitabhay.observatory.repository.AlertRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public List<AlertResponse> list(Long serviceId, String severity, Boolean resolved) {
        Specification<Alert> spec = (root, query, cb) -> cb.conjunction();

        if (serviceId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("service").get("id"), serviceId));
        }

        if (severity != null && !severity.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("severity")), severity.toLowerCase()));
        }

        if (resolved != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("resolved"), resolved));
        }

        return alertRepository.findAll(spec).stream().map(this::toResponse).toList();
    }

    public AlertResponse get(Long id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found: " + id));

        return toResponse(alert);
    }

    public AlertResponse resolve(Long id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found: " + id));

        alert.setResolved(true);
        return toResponse(alertRepository.save(alert));
    }

    private AlertResponse toResponse(Alert alert) {
        return new AlertResponse(
                alert.getId(),
                alert.getMessage(),
                alert.getSeverity(),
                alert.isResolved(),
                alert.getTriggeredAt(),
                alert.getService().getId()
        );
    }
}