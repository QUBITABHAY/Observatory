package com.qubitabhay.observatory.service;

import com.qubitabhay.observatory.model.Metric;
import com.qubitabhay.observatory.repository.MetricRepository;
import org.springframework.stereotype.Service;

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
}