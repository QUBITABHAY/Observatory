package com.qubitabhay.observatory.model;

import jakarta.persistence.*;
import org.aspectj.weaver.tools.Trace;
import org.springframework.data.geo.Metric;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "services")

public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "host_id")
    private Host host;

    @OneToMany(mappedBy = "services")
    private List<Metric> metrics;

    @OneToMany(mappedBy = "services")
    private List<LogEntry> logs;

    @OneToMany(mappedBy = "services")
    private List<Trace> traces;

    @OneToMany(mappedBy = "services")
    private List<Span> spans;
}