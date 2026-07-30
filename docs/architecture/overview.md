Artifact Name: Architecture Overview
Repository Location: docs/architecture/overview.md
Purpose: Describe the current implemented architecture, components, and data flow for the URL Shortener project.
Created By: Architecture Agent (AI-assisted), reviewed and updated by engineer
Requirement Mapping: Maps to requirement and planning artifacts.
Validation Status: Reviewed
Dependencies: docs/requirements/requirement_analysis.md, docs/requirements/ambiguity_report.md

---

# Architecture Overview

The system is implemented as a Java Spring Boot backend with a minimal Angular frontend for operating core URL shortener flows.

## Components

- Backend Service (Spring Boot)
  - API controller layer
  - Service layer for business logic
  - Repository layer (JPA) for persistence
  - Analytics capture and aggregation
  - Health endpoint via actuator

- Persistence
  - H2 database for local development and CI
  - Flyway for schema migrations

- Frontend (Angular)
  - URL shortening form
  - Metrics lookup with API key
  - URL delete action with API key

- Observability
  - Actuator health and basic Spring logs

## Data Flow

1. Client calls `POST /api/v1/shorten`.
2. Backend validates input and persists the URL record.
3. Client receives the shortened URL token.
4. Client requests `/{token}`.
5. Backend resolves the token, records analytics, updates aggregates, and redirects.
6. Analytics API returns aggregated metrics for reporting.

## Security and Operational Controls

- Input validation for alias and request body.
- Management endpoints protected by `X-API-KEY` via interceptor.
- Redirect analytics store hashed IP values rather than raw addresses.

## Scalability Path

- Add rate limiting for public endpoints.
- Add distributed cache for cross-node token lookups.
- Introduce event-driven analytics processing for high-volume traffic.

## Deployment Profiles

- `dev`: H2 database, local development.
- `test`: in-memory DB and test context.

---

# Validation

- Architecture aligns with implemented modules under `src/main/java` and frontend under `frontend/src`.
- API and schema documents are in `openapi/openapi.yaml` and `schemas/db_schema.md`.
