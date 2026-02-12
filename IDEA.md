# Observability Platform

## Overview

Build a distributed observability platform in Java that collects, processes, stores, and visualizes metrics, logs, and distributed traces. The system monitors applications running across personal servers and provides centralized monitoring and analysis.

## Problem Statement

When running multiple applications across servers, it becomes difficult to:

- Monitor system performance
- Debug errors efficiently
- Track request flows across services
- Detect performance bottlenecks
- Identify failures in real time

## Scope

Focus on a simplified, custom observability stack built from scratch to deepen understanding of distributed systems and OOP-based architecture.

## Key Features

- Metrics ingestion, storage, and dashboards
- Log collection, indexing, and search
- Distributed trace collection and visualization
- Service health and latency views
- Alerting on threshold breaches

## Tech Stack

| Area | Details |
| --- | --- |
| Programming Language | Java 17 (LTS) |
| Backend Framework | Spring Boot, Spring Web (REST APIs), Spring Data JPA (ORM), Spring Validation, Spring Security (RBAC, optional later) |
| Build Tool | Maven |
| Database | PostgreSQL (stores metrics, logs, traces, users, hosts, services) |
| ORM / Persistence | Hibernate (via Spring Data JPA) |
| Communication | HTTP REST APIs, JSON format, Java HttpClient (Agent to Collector) |
| Metrics Collection (Agent Side) | OperatingSystemMXBean, MemoryMXBean, ThreadMXBean, ScheduledExecutorService |
| Log Collection | Java NIO WatchService, file tailing mechanism |
| Distributed Tracing | Custom Trace ID and Span ID (UUID), HTTP header propagation (X-Trace-Id, X-Span-Id, X-Parent-Span-Id) |
| Frontend / Visualization | Thymeleaf (server-side rendering) or simple HTML, Chart.js (graphs and dashboards) |

## Architecture Principles

- Modular services with clear boundaries
- Backpressure-aware ingestion pipelines
- Event-driven processing where applicable
- Horizontal scalability for collectors and processors
- Fault tolerance and graceful degradation

## User Roles

### Platform Admin

Responsible for managing and configuring the observability platform itself.

Responsibilities:

- Configure collectors and ingestion endpoints
- Manage storage configuration and data retention policies
- Register and manage hosts/services
- Define and manage alert rules
- Manage user accounts and roles
- Access all metrics, logs, and traces

### Developer

Responsible for instrumenting applications and debugging service-level issues.

Responsibilities:

- Instrument services for metrics and tracing
- View service-specific metrics
- Inspect logs for assigned services
- Analyze distributed traces
- Correlate logs with trace IDs

Access Scope:

- Limited to assigned services

### Operator

Responsible for operational monitoring and incident response.

Responsibilities:

- Monitor system-wide health metrics
- Track infrastructure performance (CPU, memory, etc.)
- View logs during incidents
- Respond to alerts
- Verify service availability

Access Scope:

- Read-only access to monitoring data
- Cannot modify platform configuration
