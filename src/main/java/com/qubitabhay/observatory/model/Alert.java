package com.qubitabhay.observatory.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private String severity;
    private boolean resolved;
    private LocalDateTime triggeredAt;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity service;
}
