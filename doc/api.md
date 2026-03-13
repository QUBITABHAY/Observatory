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
