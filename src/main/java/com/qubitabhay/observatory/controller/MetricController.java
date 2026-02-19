package com.qubitabhay.observatory.controller;

import com.qubitabhay.observatory.model.Metric;
import com.qubitabhay.observatory.service.MetricService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public List<Metric> getMetrics(@RequestParam(required = false) String name, @RequestParam(required = false) String start, @RequestParam(required = false) String end) {
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;

        if (start != null && end != null) {
            startTime = LocalDateTime.parse(start);
            endTime = LocalDateTime.parse(end);
        }

        return metricService.searchMetric(name, startTime, endTime);
    }
}