package com.qubitabhay.observatory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @OneToMany(mappedBy = "service")
    private List<Metric> metrics;

    @JsonIgnore
    @OneToMany(mappedBy = "service")
    private List<LogEntry> logs;

    @JsonIgnore
    @OneToMany(mappedBy = "service")
    private List<Trace> traces;

    @JsonIgnore
    @OneToMany(mappedBy = "service")
    private List<Span> spans;
}