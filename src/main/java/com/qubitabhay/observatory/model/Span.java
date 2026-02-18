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
@Table(name = "spans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Span {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Span ID is required")
    private String spanId;

    private String parentSpanId;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private Long duration;

    @CreationTimestamp
    private LocalDateTime timestamp;

    @NotNull(message = "Trace is required")
    @ManyToOne
    @JoinColumn(name = "trace_id")
    private Trace trace;

    @NotNull(message = "Service is required")
    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity service;
}
