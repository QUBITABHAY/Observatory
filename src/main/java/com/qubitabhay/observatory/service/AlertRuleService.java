package com.qubitabhay.observatory.service;

import com.qubitabhay.observatory.dto.alert.AlertRuleResponse;
import com.qubitabhay.observatory.dto.alert.CreateAlertRuleRequest;
import com.qubitabhay.observatory.model.AlertRule;
import com.qubitabhay.observatory.model.ServiceEntity;
import com.qubitabhay.observatory.repository.AlertRuleRepository;
import com.qubitabhay.observatory.repository.ServiceEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertRuleService {

    private final AlertRuleRepository alertRuleRepository;
    private final ServiceEntityRepository serviceEntityRepository;

    public AlertRuleService(AlertRuleRepository alertRuleRepository,
                            ServiceEntityRepository serviceEntityRepository) {
        this.alertRuleRepository = alertRuleRepository;
        this.serviceEntityRepository = serviceEntityRepository;
    }

    public AlertRuleResponse create(CreateAlertRuleRequest request) {
        validateOperator(request.operator());

        ServiceEntity service = serviceEntityRepository.findById(request.serviceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found: " + request.serviceId()));

        AlertRule rule = new AlertRule();
        rule.setMetricName(request.metricName());
        rule.setOperator(request.operator());
        rule.setThreshold(request.threshold());
        rule.setSeverity(request.severity());
        rule.setService(service);

        return toResponse(alertRuleRepository.save(rule));
    }

    public List<AlertRuleResponse> list(Long serviceId) {
        List<AlertRule> rules = (serviceId != null)
                ? alertRuleRepository.findByService_Id(serviceId)
                : alertRuleRepository.findAll();

        return rules.stream().map(this::toResponse).toList();
    }

    public AlertRuleResponse get(Long id) {
        AlertRule rule = alertRuleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alert rule not found: " + id));

        return toResponse(rule);
    }

    public void delete(Long id) {
        AlertRule rule = alertRuleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alert rule not found: " + id));

        alertRuleRepository.delete(rule);
    }

    private AlertRuleResponse toResponse(AlertRule rule) {
        return new AlertRuleResponse(
                rule.getId(),
                rule.getMetricName(),
                rule.getOperator(),
                rule.getThreshold(),
                rule.getSeverity(),
                rule.getService().getId()
        );
    }

    private void validateOperator(String operator) {
        if (!">".equals(operator) && !"<".equals(operator) && !">=".equals(operator)) {
            throw new IllegalArgumentException("operator must be one of: >, <, >=");
        }
    }
}