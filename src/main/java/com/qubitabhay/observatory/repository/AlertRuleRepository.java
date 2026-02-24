package com.qubitabhay.observatory.repository;

import com.qubitabhay.observatory.model.AlertRule;
import com.qubitabhay.observatory.model.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRuleRepository extends JpaRepository<AlertRule, Long> {

    List<AlertRule> findByMetricNameAndService(String metricName, ServiceEntity service);
}
