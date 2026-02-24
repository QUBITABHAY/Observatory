package com.qubitabhay.observatory.alert.evaluator;

import com.qubitabhay.observatory.model.Alert;
import com.qubitabhay.observatory.model.AlertRule;
import com.qubitabhay.observatory.model.Metric;
import com.qubitabhay.observatory.repository.AlertRepository;
import com.qubitabhay.observatory.repository.AlertRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertEvaluator {

    @Autowired
    private AlertRuleRepository alertRuleRepository;

    @Autowired
    private AlertRepository alertRepository;

    public void evaluate(Metric metric) {
        List<AlertRule> rules = alertRuleRepository.findByMetricNameAndService(metric.getMetricName(), metric.getService());

        for (AlertRule rule : rules) {
            boolean triggered = false;

            switch (rule.getOperator()) {
                case ">":
                    triggered = metric.getValue() > rule.getThreshold();
                    break;
                case "<":
                    triggered = metric.getValue() < rule.getThreshold();
                    break;
                case ">=":
                    triggered = metric.getValue() >= rule.getThreshold();
                    break;
            }

            if (triggered) {
                Alert alert = new Alert();

                alert.setMessage(
                        "Threshold crossed for " + metric.getMetricName()
                );
                alert.setSeverity(rule.getSeverity());
                alert.setTriggeredAt(LocalDateTime.now());
                alert.setService(metric.getService());
                alert.setResolved(false);

                alertRepository.save(alert);
            }
        }
    }
}
