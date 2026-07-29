Artifact Name: Architecture Overview
Repository Location: docs/architecture/overview.md
Purpose: Describe the system architecture, components, data flow, and deployment considerations for the URL Shortener project.
Created By: Architecture Agent (AI-assisted)
Requirement Mapping: Maps to requirement and planning artifacts.
Validation Status: Draft
Dependencies: docs/requirements/requirement_analysis.md, docs/requirements/ambiguity_report.md
Recommended Commit: docs(architecture): add system architecture overview
Recommended Branch: feature/architecture -> target: main

---

# Architecture Overview

This architecture supports a Java Spring Boot backend with a minimal Angular frontend for a URL Shortener service.

## Components

- Backend Service (Spring Boot)
  - API controller layer
  - Service layer for business logic
  - Repository layer (JPA) for persistence
  - Caching for redirect performance (Caffeine)
  - Analytics capture and aggregation
  - Health and metrics endpoints

- Persistence
  - H2 database for local development and CI
  - Postgres profile for production-like deployments
  - Flyway for schema migrations

- Frontend (Angular)
  - URL shortening form
  - Metrics display page

- Observability
  - Micrometer metrics
  - Structured logs
  - Health check endpoint

## Data Flow

1. Client calls `POST /api/v1/shorten`.
2. Backend validates input and persists the URL record.
3. Client receives the shortened URL token.
4. Client requests `/{token}`.
5. Backend resolves the token, records analytics, updates aggregates, and redirects.
6. Analytics API returns aggregated metrics for reporting.

## Security and Operational Controls

- Input validation to prevent open redirects and invalid aliases.
- Management endpoints protected by a simple `X-API-KEY` header.
- Rate-limiting for public endpoints.
- PII minimization for analytics events.

## Scalability Path

- Add Redis or distributed cache for cross-node token caching.
- Introduce an event-driven analytics pipeline for high-volume click processing.
- Partition analytics storage by date or URL for scale.

## Deployment Profiles

- `dev`: H2 database, local development.
- `postgres`: Postgres database, Flyway migration.
- `test`: in-memory DB and test context.

---

# Validation

- Review architecture against acceptance criteria.
- Confirm the architecture supports traceability and incremental delivery.
