package com.qubitabhay.observatory.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table
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
