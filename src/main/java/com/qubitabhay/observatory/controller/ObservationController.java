package com.qubitabhay.observatory.controller;

import com.qubitabhay.observatory.service.LogMonitoringService;
import com.qubitabhay.observatory.service.ProcessMonitoringService;
import com.qubitabhay.observatory.service.SystemMetricsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/observe")
public class ObservationController {

    private final SystemMetricsService metricsService;
    private final ProcessMonitoringService processService;
    private final LogMonitoringService logService;

    public ObservationController(SystemMetricsService metricsService,
                                 ProcessMonitoringService processService,
                                 LogMonitoringService logService) {
        this.metricsService = metricsService;
        this.processService = processService;
        this.logService = logService;
    }

    @GetMapping("/metrics")
    public Map<String, Object> getMetrics() {
        Map<String, Object> data = new HashMap<>();
        data.put("cpu", metricsService.getCpuUsage());
        data.put("usedMemory", metricsService.getUsedMemory());
        data.put("totalMemory", metricsService.getTotalMemory());
        data.put("timestamp", LocalDateTime.now());
        return data;
    }

    @GetMapping("/processes")
    public List<Map<String, Object>> getProcesses() {
        return processService.getTopProcesses();
    }

    @GetMapping("/logs")
    public List<String> getLogs() throws IOException {
        return logService.getLastLogs(20);
    }
}
