package com.qubitabhay.observatory.controller;

import com.qubitabhay.observatory.dto.alert.AlertResponse;
import com.qubitabhay.observatory.service.AlertService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public List<AlertResponse> list(
            @RequestParam(required = false) Long serviceId,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) Boolean resolved) {
        return alertService.list(serviceId, severity, resolved);
    }

    @GetMapping("/{id}")
    public AlertResponse get(@PathVariable Long id) {
        return alertService.get(id);
    }

    @PatchMapping("/{id}/resolve")
    public AlertResponse resolve(@PathVariable Long id) {
        return alertService.resolve(id);
    }
}