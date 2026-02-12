# Observability Platform

## Overview
Observatory is a Java-based, distributed observability platform that collects, processes, stores, and visualizes metrics, logs, and distributed traces for services running across personal servers.

## Problem Statement
When running multiple applications across servers, it becomes difficult to:
- Monitor system performance
- Debug errors efficiently
- Track request flows across services
- Detect performance bottlenecks
- Identify failures in real time

## Scope
This project focuses on a simplified, custom observability stack built from scratch to deepen understanding of distributed systems and OOP-based architecture.

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
- Platform Admin: configures collectors, storage, and retention
- Developer: instruments services and inspects traces/logs
- Operator: monitors system health and responds to alerts

## Getting Started
### Prerequisites
- Java 17+
- Maven (or the included wrapper)

### Build
```bash
./mvnw clean package
```

### Run
```bash
./mvnw spring-boot:run
```

## Configuration
Application settings live in [src/main/resources/application.properties](src/main/resources/application.properties).

## Project Notes
For detailed concept documentation, see [IDEA.md](IDEA.md).

