package com.qubitabhay.observatory.service;

import com.qubitabhay.observatory.alert.evaluator.AlertEvaluator;
import com.qubitabhay.observatory.model.Host;
import com.qubitabhay.observatory.model.Metric;
import com.qubitabhay.observatory.model.ServiceEntity;
import com.qubitabhay.observatory.repository.HostRepository;
import com.qubitabhay.observatory.repository.MetricRepository;
import com.qubitabhay.observatory.repository.ServiceEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MetricService {

    private final MetricRepository metricRepository;
    private final HostRepository hostRepository;
    private final ServiceEntityRepository serviceEntityRepository;

    public MetricService(MetricRepository metricRepository,
                         HostRepository hostRepository,
                         ServiceEntityRepository serviceEntityRepository) {
        this.metricRepository = metricRepository;
        this.hostRepository = hostRepository;
        this.serviceEntityRepository = serviceEntityRepository;
    }

    @Autowired
    private AlertEvaluator alertEvaluator;

    public Metric saveMetric(Metric metric) {
        if (metric.getHost() == null || metric.getHost().getId() == null) {
            throw new IllegalArgumentException("host.id is required");
        }

        if (metric.getService() == null || metric.getService().getId() == null) {
            throw new IllegalArgumentException("service.id is required");
        }

        Host host = hostRepository.findById(metric.getHost().getId())
                .orElseThrow(() -> new IllegalArgumentException("Host not found: " + metric.getHost().getId()));

        ServiceEntity serviceEntity = serviceEntityRepository.findById(metric.getService().getId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found: " + metric.getService().getId()));

        metric.setHost(host);
        metric.setService(serviceEntity);

        Metric savedMetric = metricRepository.save(metric);

        alertEvaluator.evaluate(savedMetric);

        return savedMetric;
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