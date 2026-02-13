```mermaid
classDiagram

class Host {
  +id: Integer
  +hostname: String
  +ipAddress: String
  +environment: String
  +createdAt: LocalDateTime
}

class Service {
  +id: Integer
  +name: String
  +host_id: Integer
  +createdAt: LocalDateTime
  +sendMetrics()
  +sendLogs()
  +sendTraces()
}

class Log {
  +id: Integer
  +service_id: Integer
  +host_id: Integer
  +level: String
  +message: String
  +traceId: String
  +timestamp: LocalDateTime
}

class Metric {
  +id: Integer
  +service_id: Integer
  +host_id: Integer
  +metricName: String
  +metricType: String
  +value: Double
  +timestamp: LocalDateTime
}

class Trace {
  +id: Integer
  +traceId: String
  +service_id: Integer
  +startedAt: LocalDateTime
  +duration: Long
}

class Span {
  +id: Integer
  +trace_id: Integer
  +service_id: Integer
  +spanId: String
  +parentSpanId: String
  +duration: Long
  +timestamp: LocalDateTime
}

class LogCollector {
  +collectLog()
  +storeLog()
}

class MetricsCollector {
  +collectMetric()
  +storeMetric()
}

class TraceCollector {
  +collectTrace()
  +storeTrace()
}

class AlertRule {
  +ruleId: Integer
  +condition: String
  +threshold: Double
  +evaluate()
}

class AlertManager {
  +triggerAlert()
  +notify()
}

class Dashboard {
  +viewMetrics()
  +viewLogs()
  +viewTraces()
}

class User {
  +userId: Integer
  +name: String
  +role: String
}

class PlatformAdmin {
}

class Developer {
}

class Operator {
}

Host --> Service
Host --> Metric
Host --> Log

Service --> Log
Service --> Metric
Service --> Trace
Service --> Span

Trace --> Span

LogCollector --> Log
MetricsCollector --> Metric
TraceCollector --> Trace
TraceCollector --> Span

MetricsCollector --> AlertRule
AlertRule --> AlertManager

User <|-- PlatformAdmin
User <|-- Developer
User <|-- Operator

User --> Dashboard
Dashboard --> LogCollector
Dashboard --> MetricsCollector
Dashboard --> TraceCollector

```

| Pattern | Where Applied | Purpose |
| --- | --- | --- |
| Observer | AlertManager -> User notifications | Decouple alert evaluation from notification delivery. |
| Strategy | AlertRule evaluation logic | Allow multiple rule types and thresholds without changing collectors. |
| Facade | Dashboard access to collectors | Provide a simplified view for metrics, logs, and traces retrieval. |

| Principle | Application |
| --- | --- |
| Encapsulation | Collectors and domain objects expose behavior through methods, hiding internal storage details. |
| Abstraction | Collectors define high-level operations like `collectMetric()` and `storeMetric()`. |
| Inheritance | Roles modeled by extending `User` (PlatformAdmin, Operator, Developer). |
| Polymorphism | Different `AlertRule` variants can evaluate conditions in their own way. |
