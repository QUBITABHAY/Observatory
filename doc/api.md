# Observatory API

## Base Path

- `http://localhost:8080`

## Common Error Response

When validation or request data is invalid, APIs return:

```json
{
  "timestamp": "2026-03-14T01:44:50.130922",
  "status": 400,
  "error": "Bad Request",
  "message": "Host not found: 999"
}
```

## Metrics API

### Create Metric

- Method: `POST`
- Path: `/api/metrics`
- Content-Type: `application/json`

Request body:

```json
{
  "metricName": "cpu_usage",
  "metricType": "gauge",
  "value": 41.2,
  "service": { "id": 1 },
  "host": { "id": 1 }
}
```

Validation rules:

- `metricName`: required, non-blank
- `metricType`: required, non-blank
- `value`: required, positive
- `service.id`: required and must exist
- `host.id`: required and must exist

Success response:

- Status: `200 OK`
- Body: saved `Metric` object

Error response:

- Status: `400 Bad Request`
- Cases:
  - missing `host.id` or `service.id`
  - nonexistent host or service

### Get/Search Metrics

- Method: `GET`
- Path: `/api/metrics`
- Query params:
  - `name` (optional)
  - `start` (optional, ISO-8601 `LocalDateTime`)
  - `end` (optional, ISO-8601 `LocalDateTime`)

Behavior:

- `name + start + end`: filter by metric name and time range
- `name` only: filter by metric name
- `start + end` only: filter by time range
- no params: return all metrics

Example:

- `/api/metrics?name=cpu_usage&start=2026-02-20T10:00:00&end=2026-02-20T12:00:00`

Success response:

- Status: `200 OK`
- Body: list of `Metric`

## Hosts API

### Create Host

- Method: `POST`
- Path: `/api/hosts`
- Content-Type: `application/json`

Request body:

```json
{
  "hostname": "srv-1",
  "ipAddress": "10.0.0.11",
  "environment": "home-lab"
}
```

Success response:

- Status: `200 OK`
- Body:

```json
{
  "id": 1,
  "hostname": "srv-1",
  "ipAddress": "10.0.0.11",
  "environment": "home-lab",
  "createdAt": "2026-03-14T01:40:02.94824"
}
```

### List Hosts

- Method: `GET`
- Path: `/api/hosts`

Success response:

- Status: `200 OK`
- Body: list of hosts

### Get Host By ID

- Method: `GET`
- Path: `/api/hosts/{id}`

Success response:

- Status: `200 OK`
- Body: host object

## Services API

### Create Service

- Method: `POST`
- Path: `/api/services`
- Content-Type: `application/json`

Request body:

```json
{
  "name": "node-agent",
  "hostId": 1
}
```

Success response:

- Status: `200 OK`
- Body:

```json
{
  "id": 1,
  "name": "node-agent",
  "hostId": 1,
  "createdAt": "2026-03-14T01:40:06.3506"
}
```

Error response:

- Status: `400 Bad Request`
- Case: host does not exist

### List Services

- Method: `GET`
- Path: `/api/services`
- Query params:
  - `hostId` (optional)

Behavior:

- `hostId` present: list services for that host
- no query param: list all services

Success response:

- Status: `200 OK`
- Body: list of services

### Get Service By ID

- Method: `GET`
- Path: `/api/services/{id}`

Success response:

- Status: `200 OK`
- Body: service object

## Logs API

### Ingest Log Entry

- Method: `POST`
- Path: `/api/logs`
- Content-Type: `application/json`

Request body:

```json
{
  "level": "ERROR",
  "message": "Connection timeout to DB",
  "traceId": "trace-abc-123",
  "hostId": 1,
  "serviceId": 1
}
```

Validation rules:

- `level`: required, non-blank
- `message`: required, non-blank
- `traceId`: optional
- `hostId`: required, must exist
- `serviceId`: required, must exist

Success response:

- Status: `201 Created`
- Body:

```json
{
  "id": 1,
  "level": "ERROR",
  "message": "Connection timeout to DB",
  "traceId": "trace-abc-123",
  "hostId": 1,
  "serviceId": 1,
  "timestamp": "2026-03-15T16:16:26.990924"
}
```

### Query Logs

- Method: `GET`
- Path: `/api/logs`
- Query params:
  - `hostId` (optional)
  - `serviceId` (optional)
  - `level` (optional)

Behavior:

- All params are combinable; no params returns all logs
- `level` is case-insensitive

Success response:

- Status: `200 OK`
- Body: list of log response objects

---

## Traces API

### Ingest Trace

- Method: `POST`
- Path: `/api/traces`
- Content-Type: `application/json`

Request body:

```json
{
  "traceId": "trace-check-1773573163",
  "duration": 321,
  "serviceId": 1
}
```

Validation rules:

- `traceId`: required, non-blank
- `duration`: required, positive integer (milliseconds)
- `serviceId`: required, must exist

Success response:

- Status: `201 Created`
- Body:

```json
{
  "id": 1,
  "traceId": "trace-check-1773573163",
  "duration": 321,
  "serviceId": 1,
  "startedAt": "2026-03-15T16:42:43.495216"
}
```

### Query Traces

- Method: `GET`
- Path: `/api/traces`
- Query params:
  - `serviceId` (optional)

Behavior:

- `serviceId` present: return traces for that service
- no params: return all traces

Success response:

- Status: `200 OK`
- Body: list of trace response objects

---

## Spans API

### Ingest Span

- Method: `POST`
- Path: `/api/spans`
- Content-Type: `application/json`

Request body:

```json
{
  "spanId": "span-root-01",
  "parentSpanId": null,
  "duration": 120,
  "traceId": "trace-check-1773573163",
  "serviceId": 1
}
```

Validation rules:

- `spanId`: required, non-blank
- `parentSpanId`: optional (null for root spans)
- `duration`: required, positive integer (milliseconds)
- `traceId`: required, must match an existing trace's `traceId`
- `serviceId`: required, must exist

Success response:

- Status: `201 Created`
- Body:

```json
{
  "id": 1,
  "spanId": "span-root-01",
  "parentSpanId": null,
  "duration": 120,
  "traceId": "trace-check-1773573163",
  "serviceId": 1,
  "timestamp": "2026-03-15T16:42:58.736279"
}
```

### Get Spans by Trace ID

- Method: `GET`
- Path: `/api/spans`
- Query params:
  - `traceId` (required)

Behavior:

- Returns all spans belonging to the given `traceId` string
- Missing `traceId` param returns `400 Bad Request`

Success response:

- Status: `200 OK`
- Body: list of span response objects (ordered by insertion)

---

## Observation API

### Current System Metrics

- Method: `GET`
- Path: `/api/observe/metrics`

Success response:

- Status: `200 OK`
- Body:

```json
{
  "cpu": 12.7,
  "usedMemory": 6400000000,
  "totalMemory": 17179869184,
  "timestamp": "2026-02-20T23:30:00"
}
```

### Top Processes

- Method: `GET`
- Path: `/api/observe/processes`

Success response:

- Status: `200 OK`
- Body: list of up to 10 process objects with:
  - `pid`
  - `name`
  - `cpuUsage`
  - `memory`

### Recent Application Logs

- Method: `GET`
- Path: `/api/observe/logs`

Behavior:

- Returns the last 20 lines from the configured logging file (`logging.file.name`)
- Fallback search paths:
  - `src/main/resources/logs/application.log`
  - `logs/application.log`
- If file is missing, returns `["Log file not found"]`

Success response:

- Status: `200 OK`
- Body: list of log lines

---

## Alert Rules API

### Create Alert Rule

- Method: `POST`
- Path: `/api/alert-rules`
- Content-Type: `application/json`

Request body:

```json
{
  "metricName": "cpu_usage",
  "operator": ">",
  "threshold": 85.0,
  "severity": "critical",
  "serviceId": 1
}
```

Validation rules:

- `metricName`: required, non-blank
- `operator`: required, one of `>`, `<`, `>=`
- `threshold`: required, zero or positive
- `severity`: required, non-blank
- `serviceId`: required, must exist

Success response:

- Status: `201 Created`
- Body:

```json
{
  "id": 1,
  "metricName": "cpu_usage",
  "operator": ">",
  "threshold": 85.0,
  "severity": "critical",
  "serviceId": 1
}
```

### List Alert Rules

- Method: `GET`
- Path: `/api/alert-rules`
- Query params:
  - `serviceId` (optional)

Behavior:

- `serviceId` present: list rules for that service
- no query param: list all alert rules

Success response:

- Status: `200 OK`
- Body: list of alert rule response objects

### Get Alert Rule By ID

- Method: `GET`
- Path: `/api/alert-rules/{id}`

Success response:

- Status: `200 OK`
- Body: alert rule response object

### Delete Alert Rule

- Method: `DELETE`
- Path: `/api/alert-rules/{id}`

Success response:

- Status: `204 No Content`

---

## Alerts API

### List Alerts

- Method: `GET`
- Path: `/api/alerts`
- Query params:
  - `serviceId` (optional)
  - `severity` (optional, case-insensitive)
  - `resolved` (optional, `true` or `false`)

Behavior:

- Any combination of query params is supported
- No query params returns all alerts

Success response:

- Status: `200 OK`
- Body: list of alert response objects

### Get Alert By ID

- Method: `GET`
- Path: `/api/alerts/{id}`

Success response:

- Status: `200 OK`
- Body: alert response object

### Resolve Alert

- Method: `POST` (also supports `PATCH` for backward compatibility)
- Path: `/api/alerts/{id}/resolve`

Behavior:

- Marks the alert as resolved (`resolved = true`)

Success response:

- Status: `200 OK`
- Body: updated alert response object

### Silence Alert

- Method: `POST`
- Path: `/api/alerts/{id}/silence`

Behavior:

- Marks the alert as silenced (`silenced = true`)

Success response:

- Status: `200 OK`
- Body: updated alert response object

Alert response object fields:

- `id`
- `message`
- `severity`
- `resolved`
- `silenced`
- `triggeredAt`
- `serviceId`
