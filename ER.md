```mermaid
erDiagram

    HOST {
        Integer id PK
        String hostname
        String ipAddress
        String environment
        LocalDateTime createdAt
    }

    SERVICE {
        Integer id PK
        String name
        Integer host_id FK
        LocalDateTime createdAt
    }

    METRIC {
        Integer id PK
        Integer service_id FK
        Integer host_id FK
        String metricName
        String metricType
        Double value
        LocalDateTime timestamp
    }

    LOG {
        Integer id PK
        Integer service_id FK
        Integer host_id FK
        String level
        String message
        String traceId
        LocalDateTime timestamp
    }

    TRACE {
        Integer id PK
        String traceId
        Integer service_id FK
        LocalDateTime startedAt
        Long duration
    }

    SPAN {
        Integer id PK
        Integer trace_id FK
        Integer service_id FK
        String spanId
        String parentSpanId
        Long duration
        LocalDateTime timestamp
    }

    ALERT_RULE {
        Long id PK
        String metricName
        String operator
        Double threshold
        String severity
        Integer service_id FK
    }

    ALERT {
        Long id PK
        String message
        String severity
        Boolean resolved
        LocalDateTime triggeredAt
        Integer service_id FK
    }

    HOST ||--o{ SERVICE : hosts
    HOST ||--o{ METRIC : generates
    HOST ||--o{ LOG : produces

    SERVICE ||--o{ METRIC : records
    SERVICE ||--o{ LOG : produces
    SERVICE ||--o{ TRACE : handles

    TRACE ||--o{ SPAN : contains
    SERVICE ||--o{ SPAN : executes
    SERVICE ||--o{ ALERT_RULE : defines
    SERVICE ||--o{ ALERT : triggers
```

| Entity     | Description                                                                | Key Relationships                                                                |
| ---------- | -------------------------------------------------------------------------- | -------------------------------------------------------------------------------- |
| HOST       | Infrastructure node that runs services and emits metrics/logs.             | HOST 1..\* SERVICE, HOST 1..\* METRIC, HOST 1..\* LOG                            |
| SERVICE    | Application component deployed on a host that produces observability data. | SERVICE 1..\* METRIC, SERVICE 1..\* LOG, SERVICE 1..\* TRACE, SERVICE 1..\* SPAN |
| METRIC     | Time-series measurement captured for a service or host.                    | Belongs to one HOST and one SERVICE                                              |
| LOG        | Structured event record with level, message, and optional trace context.   | Belongs to one HOST and one SERVICE                                              |
| TRACE      | End-to-end request flow captured across services.                          | TRACE 1..\* SPAN; belongs to one SERVICE                                         |
| SPAN       | Unit of work within a trace, optionally linked to a parent span.           | Belongs to one TRACE and one SERVICE                                             |
| ALERT_RULE | Condition to trigger an alert based on a metric threshold.                 | Belongs to one SERVICE                                                           |
| ALERT      | Record of a triggered alert rule.                                          | Belongs to one SERVICE                                                           |
