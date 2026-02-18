package com.qubitabhay.observatory.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Metric name is required")
    private String metricName;

    @NotBlank(message = "Metric type is required")
    private String metricType;

    @NotNull(message = "Metric value is required")
    @Positive(message = "Metric value must be positive")
    private Double value;

    @CreationTimestamp
    private LocalDateTime timestamp;

    @NotNull(message = "Service is required")
    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

    @NotNull(message = "Host is required")
    @ManyToOne
    @JoinColumn(name = "host_id")
    private Host host;
}
