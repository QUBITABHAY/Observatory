package com.qubitabhay.observatory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String spanId;
    private String parentSpanId;
    private Long duration;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "trace_id")
    private Trace trace;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity service;
}
