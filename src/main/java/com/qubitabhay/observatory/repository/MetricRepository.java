package com.qubitabhay.observatory.repository;

import com.qubitabhay.observatory.model.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {
    List<Metric> findByMetricName(String metricName);

    List<Metric> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    List<Metric> findByMetricNameAndTimestampBetween(String metricName, LocalDateTime start, LocalDateTime end);
}