# Observatory API

## Base Path

- `http://localhost:8080`

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
- `service`: required
- `host`: required

Success response:

- Status: `200 OK`
- Body: saved `Metric` object

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

- Returns the last 20 lines from `logs/application.log`
- If file is missing, returns `["Log file not found"]`

Success response:

- Status: `200 OK`
- Body: list of log lines
