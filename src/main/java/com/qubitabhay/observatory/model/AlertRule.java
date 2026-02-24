package com.qubitabhay.observatory.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AlertRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String metricName;
    private String operator;
    private Double threshold;
    private String severity;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

}
