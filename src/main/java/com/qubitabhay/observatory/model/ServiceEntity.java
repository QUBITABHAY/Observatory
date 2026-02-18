package com.qubitabhay.observatory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "host_id")
    private Host host;

    @OneToMany(mappedBy = "service")
    private List<Metric> metrics;

    @OneToMany(mappedBy = "service")
    private List<LogEntry> logs;

    @OneToMany(mappedBy = "service")
    private List<Trace> traces;

    @OneToMany(mappedBy = "service")
    private List<Span> spans;
}