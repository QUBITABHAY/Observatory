package com.qubitabhay.observatory.service;

import com.qubitabhay.observatory.model.Metric;
import com.qubitabhay.observatory.repository.MetricRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MetricService {

    private final MetricRepository metricRepository;

    public MetricService(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    public Metric saveMetric(Metric metric) {
        return metricRepository.save(metric);
    }

    public List<Metric> getAllMetric() {
        return metricRepository.findAll();
    }

    public List<Metric> searchMetric(String name, LocalDateTime start, LocalDateTime end) {
        if (name != null && start != null && end != null) {
            return metricRepository.findByMetricNameAndTimestampBetween(name, start, end);
        }

        if (name != null) {
            return metricRepository.findByMetricName(name);
        }

        if (start != null && end != null) {
            return metricRepository.findByTimestampBetween(start, end);
        }

        return metricRepository.findAll();
    }
}