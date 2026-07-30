# Software Design Document (SDD)

## 1. System Scope
This document describes the implemented software design of the URL shortener system in this repository.

## 2. Technology Stack
- Backend: Java 17+, Spring Boot 3.2.x, Spring Web, Spring Data JPA, Flyway
- Database: H2 (in-memory for local run)
- Frontend: Angular 17 standalone app
- Tests: Spring Boot integration tests and Angular/Karma unit tests

## 3. High-Level Architecture
1. Presentation Layer
- Angular SPA for create/metrics/delete workflows.

2. API Layer
- URL creation: UrlShortenerController
- Redirect: RedirectController
- Metrics: MetricsController
- Management delete: ManagementController

3. Domain/Service Layer
- UrlShortenerService for alias/token generation and persistence.
- AnalyticsService for event capture, click updates, and unique counting.

4. Persistence Layer
- JPA entities: UrlMapping, Event, AggregateCount
- Repositories: UrlMappingRepository, EventRepository, AggregateCountRepository
- Schema migration: src/main/resources/db/migration/V1__init.sql

5. Cross-Cutting Components
- ApiKeyInterceptor and SecurityConfig for endpoint protection and CORS.
- RestExceptionHandler + custom exceptions for consistent API errors.

## 4. API Endpoints
1. POST /api/v1/shorten
- Validates request.
- Detects alias conflict (409).
- Returns id, shortUrl, alias (201).

2. GET /{token}
- Resolves token or alias.
- Records analytics event.
- Returns 302 with Location.
- Returns 404 when mapping is missing.

3. GET /api/v1/urls/{id}/metrics
- Requires X-API-KEY.
- Returns clicks and uniques.
- Returns 401 for missing/invalid key.

4. DELETE /api/v1/urls/{id}
- Requires X-API-KEY.
- Returns 204 on delete success.
- Returns 404 when URL id is absent.

## 5. Data Model Design
1. urls
- Stores token/alias -> long URL mapping and cumulative click count.

2. events
- Stores redirect event with occurred_at and hashed ip_hash.

3. aggregates
- Stores daily aggregate clicks/uniques per URL.

## 6. Core Flow Design
1. Shorten flow
- Controller receives request -> service validates -> repository persists -> response composed with absolute short URL.

2. Redirect flow
- Controller resolves token -> analytics service records event and updates counters -> 302 redirect response.

3. Metrics flow
- Controller enforces API key -> fetches mapping and unique visitor count -> returns metrics DTO.

4. Delete flow
- Controller checks id presence -> deletes mapping -> returns no-content/not-found.

## 7. Error Handling Strategy
- Validation errors return structured JSON (400).
- Alias conflict uses ConflictException (409).
- Missing resources use ResourceNotFoundException (404).
- Unauthorized management/metrics requests return 401 through interceptor/controller checks.

## 8. Security and Privacy Notes
1. API key requirement for management endpoints.
2. CORS allows local frontend origin.
3. Redirect analytics hashes remote IP before storage.

## 9. Test Design
- Backend integration tests validate:
  - create + redirect success
  - alias conflict
  - missing-token not found
  - metrics auth and value correctness
  - delete auth and repeated delete behavior
- Frontend test validates component creation and rendered heading.

## 10. Deployment and Runtime Notes
- Backend runs on localhost:8080.
- Frontend runs on localhost:4200.
- H2 state resets per backend restart.

## 11. Known Limitations
1. No rate limiting.
2. Shared API key model only.
3. No distributed cache.
4. No production deployment manifests in repository.
