Artifact Name: Architecture Overview
Repository Location: docs/architecture/overview.md
Purpose: High-level system architecture, component responsibilities, data flow, and deployment considerations for the URL Shortener prototype.
Created By: Architecture Agent (AI-assisted)
Requirement Mapping: Maps to WBS and requirement analysis artifacts.
Validation Status: Draft — requires engineer review and sign-off
Dependencies: docs/requirements/requirement_analysis.md, docs/requirements/ambiguity_report.md
Recommended Commit: docs(architecture): add system overview and ADRs
Recommended Branch: feature/architecture -> target: main

---

# System Overview

This document describes a prototype architecture for the AI_Assisted_URL_Shortner system. The design favors clarity, testability, and an easy migration path to production.

## Components

- API Gateway (optional for later): routing, TLS termination, API key enforcement.
- Backend Service (Spring Boot): implements URL creation, redirect, analytics capture, management endpoints, health checks, and metrics.
- Persistence Layer:
  - Primary relational DB (Postgres recommended for production).
  - H2 for local development and CI tests (configurable profile).
  - Flyway for schema migrations.
- Cache Layer: in-memory cache (Caffeine) for hot redirect paths.
- Analytics: events stored in `events` table; aggregated counters in `aggregates` table for reporting.
- Frontend (Angular, minimal): UI for creating short URLs and viewing metrics.
- Observability: Micrometer for metrics, structured logs (Logback), optional OpenTelemetry hooks.

## Data Flow

1. Client calls `POST /api/v1/shorten` with a long URL (and optional alias).
2. Backend validates input, generates token or validates alias, persists record to DB.
3. Client receives short URL.
4. Redirect: Client requests `/{token}`; service checks cache, queries DB if miss, records click event, increments aggregate counters, and issues a 301/302 redirect.
5. Analytics API queries aggregates or raw events as requested (protected endpoints require API key).

## Deployment & Profiles

- Profiles: `dev` (H2), `postgres` (Postgres + Flyway), `test` (in-memory DB for CI).
- Containerization: Dockerfile for the backend and frontend; compose for local integration (Postgres + service + frontend).
- CI: GitHub Actions to run build and tests; optional matrix job to run integration tests against Postgres container.

## Security Considerations

- Validate target URLs to avoid open-redirect vulnerabilities.
- Management endpoints protected by API key; upgrade to OAuth2 in production.
- Rate-limiting for public endpoints to mitigate abuse.
- Do not store PII (e.g., full IP) without consent — provide hashing/opt-out options.

## Scalability Path

- Move analytics to an event pipeline (Kafka) for high-volume event ingestion.
- Use Redis or distributed cache for cross-node hot token caching.
- Shard or partition events and aggregates as volumes grow.

## Observability & Operations

- Expose `/actuator/health` and `/actuator/metrics` (Micrometer).
- Structured logging for key events; include correlation IDs.
- Provide runbook entries in docs/validation/ for common failures.

---

# Artifact Contract

Artifact Name: Architecture Overview
Repository Location: docs/architecture/overview.md
Purpose: Guide implementation and architecture reviews
Created By: Architecture Agent (AI-assisted)
Validation Status: Draft — requires engineer review

Please review and approve or request edits. Once approved, I'll create ADRs for the major decisions (DB choice, token generation strategy, analytics retention and auth model).