package com.qubitabhay.observatory.controller;

import com.qubitabhay.observatory.model.Metric;
import com.qubitabhay.observatory.service.MetricService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/metrics")
public class MetricController {
    private static final Logger logger = LoggerFactory.getLogger(MetricController.class);
    private final MetricService metricService;

    public MetricController(MetricService metricService) {
        this.metricService = metricService;
    }

    @PostMapping
    public Metric createMetric(@Valid @RequestBody Metric metric) {
        logger.info("Creating metric: {}", metric);
        return metricService.saveMetric(metric);
    }

    @GetMapping
    public List<Metric> getMetrics(
            @RequestParam(required = false) String name,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        LocalDateTime effectiveFrom = from != null ? from : start;
        LocalDateTime effectiveTo = to != null ? to : end;

        return metricService.searchMetric(name, effectiveFrom, effectiveTo, page, size);
    }
}