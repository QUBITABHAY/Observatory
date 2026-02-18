package com.qubitabhay.observatory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String traceId;
    private LocalDateTime startedAt;
    private Long duration;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

    @OneToMany(mappedBy = "trace")
    private List<Span> spans;
}
