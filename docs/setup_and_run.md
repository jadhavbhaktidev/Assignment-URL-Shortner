# Assignment 1 Setup and Run Instructions

## Prerequisites
- Java 17+ available on PATH
- Maven 3.9+
- Node.js 18+ and npm

## Repository layout
- Backend root: repository root
- Frontend root: `frontend/`

## Backend setup
1. From repository root, run:
   - `mvn clean package`
2. Start backend:
   - `mvn spring-boot:run`
3. Verify health:
   - `GET http://localhost:8080/actuator/health`

Backend quality validation:
- Run full backend tests: `mvn test`
- Run OpenAPI contract checks only: `mvn -Dtest=OpenApiContractTest test`

## Frontend setup
1. From `frontend/`, install dependencies:
   - `npm install`
2. Start dev server:
   - `npm start`
3. Open:
   - `http://localhost:4200`

## End-to-end local flow
1. In UI, submit long URL and optional alias.
2. Use returned URL ID in the metrics panel.
3. Provide API key `change-me` for metrics and delete requests.
4. Open generated short URL to trigger redirect and analytics event.

## API key configuration
- Default local fallback key is configured in `src/main/resources/application.yml`.
- Recommended secure configuration for non-local environments:
   - `URLSHORTENER_API_KEY=<strong-secret-value>`

Behavior:
- On startup, the app logs a warning if the default key is used.
- In `prod` profile, default key usage fails startup.

## Rate limiting and request tracing
- Public endpoints (`POST /api/v1/shorten`, `GET /{token}`) are protected by in-memory rate limiting.
- Default limits are configured under `urlshortener.rate-limit` in `application.yml`.
- Every response includes `X-Request-ID`; if a request provides one, it is echoed.

## CORS
- Backend allows frontend origin `http://localhost:4200` through `CorsFilter` in:
  - `src/main/java/com/aiassisted/urlshortener/config/SecurityConfig.java`

## Notes for reproducibility
- Schema is applied through Flyway migration `V1__init.sql`.
- H2 in-memory database is reset on process restart.

## CI quality gates
- Workflow file: `.github/workflows/ci.yml`
- Triggered on push and pull request.
- Executes:
   - backend test suite (including OpenAPI contract tests)
   - frontend unit tests in ChromeHeadless
   - frontend production build
