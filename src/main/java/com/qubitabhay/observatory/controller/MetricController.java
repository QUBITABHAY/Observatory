package com.qubitabhay.observatory.controller;

import com.qubitabhay.observatory.model.Metric;
import com.qubitabhay.observatory.service.MetricService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metrics")
public class MetricController {
    private final MetricService metricService;

    public MetricController(MetricService metricService) {
        this.metricService = metricService;
    }

    @PostMapping
    public Metric createMetric(@RequestBody Metric metric) {
        System.out.println("print");
        System.out.println(metric);
        System.out.println("done");
        return metricService.saveMetric(metric);
    }

    @GetMapping
    public List<Metric> getMetrics() {
        return metricService.getAllMetric();
    }
}