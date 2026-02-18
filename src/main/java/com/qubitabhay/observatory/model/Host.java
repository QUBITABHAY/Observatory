package com.qubitabhay.observatory.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "hosts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Host {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Hostname is required")
    private String hostname;

    @NotBlank(message = "IP address is required")
    private String ipAddress;

    @NotBlank(message = "Environment is required")
    private String environment;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "host")
    private List<ServiceEntity> services;

    @OneToMany(mappedBy = "host")
    private List<Metric> metrics;

    @OneToMany(mappedBy = "host")
    private List<LogEntry> logs;
}