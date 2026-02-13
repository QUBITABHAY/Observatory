```mermaid
graph TB

    Dev([Developer])
    Operator([Operator])
    Admin([Platform Admin])
    App([Application Services])

    subgraph Observability_Stack
        CollectLogs((Collect Logs))
        CollectMetrics((Collect Metrics))
        CollectTraces((Collect Traces))

        ViewDashboards((View Dashboards))
        CreateAlerts((Create Alerts))
        ReceiveAlerts((Receive Alert Notifications))

        SearchLogs((Search Logs))
        AnalyzeMetrics((Analyze Metrics))
        TraceRequests((Trace Requests))

        ManageStack((Manage Observability Stack))
    end

    App --> CollectLogs
    App --> CollectMetrics
    App --> CollectTraces

    Dev --> ViewDashboards
    Dev --> SearchLogs
    Dev --> AnalyzeMetrics
    Dev --> TraceRequests

    Operator --> ViewDashboards
    Operator --> ReceiveAlerts
    Operator --> AnalyzeMetrics

    Admin --> ManageStack
    Admin --> CreateAlerts
    Admin --> ViewDashboards
    Admin --> SearchLogs
    Admin --> AnalyzeMetrics
    Admin --> TraceRequests

```

| Actor | Use Cases |
| --- | --- |
| Application Services | Collect Logs, Collect Metrics, Collect Traces |
| Developer | View Dashboards, Search Logs, Analyze Metrics, Trace Requests |
| Operator | View Dashboards, Receive Alert Notifications, Analyze Metrics |
| Platform Admin | Manage Observability Stack, Create Alerts, Access All Data |