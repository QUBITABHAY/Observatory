package com.qubitabhay.observatory.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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

    @NotBlank(message = "Service name is required")
    private String name;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @NotNull(message = "Host is required")
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