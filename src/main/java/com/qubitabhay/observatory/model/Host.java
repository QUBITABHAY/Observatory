package com.qubitabhay.observatory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "hosts")
public class Host {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hostname;
    private String ipAddress;
    private String environment;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "host")
    private List<ServiceEntity> services;

    @OneToMany(mappedBy = "host")
    private List<Metric> metrics;

    @OneToMany(mappedBy = "host")
    private List<LogEntry> logs;
}