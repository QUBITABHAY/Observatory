package com.qubitabhay.observatory.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Log level is required")
    private String level;

    @NotBlank(message = "Log message is required")
    private String message;

    private String traceId;

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
