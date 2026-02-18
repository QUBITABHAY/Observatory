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
import java.util.List;

@Entity
@Table(name = "traces")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Trace ID is required")
    private String traceId;

    @CreationTimestamp
    @NotNull(message = "Started at timestamp is required")
    private LocalDateTime startedAt;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private Long duration;

    @NotNull(message = "Service is required")
    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

    @OneToMany(mappedBy = "trace")
    private List<Span> spans;
}
