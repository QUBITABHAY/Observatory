package com.qubitabhay.observatory.controller;

import com.qubitabhay.observatory.dto.alert.AlertRuleResponse;
import com.qubitabhay.observatory.dto.alert.CreateAlertRuleRequest;
import com.qubitabhay.observatory.service.AlertRuleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/api/alert-rules", "/api/alert-rules/", "/api/alerts-rules", "/api/alerts-rules/"})
public class AlertRuleController {

    private final AlertRuleService alertRuleService;

    public AlertRuleController(AlertRuleService alertRuleService) {
        this.alertRuleService = alertRuleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlertRuleResponse create(@Valid @RequestBody CreateAlertRuleRequest request) {
        return alertRuleService.create(request);
    }

    @GetMapping
    public List<AlertRuleResponse> list(@RequestParam(required = false) Long serviceId) {
        return alertRuleService.list(serviceId);
    }

    @GetMapping("/{id}")
    public AlertRuleResponse get(@PathVariable Long id) {
        return alertRuleService.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        alertRuleService.delete(id);
    }
}