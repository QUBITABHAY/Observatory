```mermaid
sequenceDiagram
    participant User
    participant App as Service
    participant Metrics as Metrics Collector
    participant Logs as Log Collector
    participant Tracer as Trace Collector
    participant Alert as Alert Manager
    participant Dashboard as Monitoring Dashboard

    User->>App: Send Request
    App->>Metrics: Send Metrics (latency, CPU, errors)
    App->>Logs: Send Logs (request/response, errors)
    App->>Tracer: Send Trace Data

    Metrics->>Alert: Evaluate Alert Rules
    Alert-->>User: Send Alert Notification (if threshold exceeded)

    User->>Dashboard: Open Dashboard
    Dashboard->>Metrics: Fetch Metrics Data
    Dashboard->>Logs: Fetch Logs
    Dashboard->>Tracer: Fetch Trace Data
    Dashboard-->>User: Display Metrics, Logs, Traces

```

| Flow Step            | Description                                                        |
| -------------------- | ------------------------------------------------------------------ |
| User sends request   | User initiates a request to the application service.               |
| App emits telemetry  | Application sends metrics, logs, and trace data to collectors.     |
| Alert evaluation     | Metrics collector evaluates rules and triggers alerts when needed. |
| Alert notification   | Alert manager notifies the user if thresholds are exceeded.        |
| Dashboard access     | User opens the dashboard to observe system health.                 |
| Dashboard data fetch | Dashboard pulls metrics, logs, and traces from collectors.         |
| Dashboard display    | Dashboard presents combined observability data to the user.        |
