package com.qubitabhay.observatory.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "metrics")
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String metricName;
    private String metricType;
    private Double value;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

    @ManyToOne
    @JoinColumn(name = "host_id")
    private Host host;
}
