# AI-Assisted URL Shortener

## Overview

A prototype URL shortener backend built with Java Spring Boot. It supports:

- Short URL creation with optional custom alias
- Redirects via token or alias
- API key-protected metrics endpoint
- Event capture and aggregate analytics
- H2-based local persistence and Flyway migrations

## Getting Started

### Prerequisites

- Java 17
- Maven

### Run locally

```bash
mvn spring-boot:run
```

The application starts at `http://localhost:8080`.

### Create a short URL

POST `http://localhost:8080/api/v1/shorten`

Request body:

```json
{
  "longUrl": "https://example.com",
  "customAlias": "example"
}
```

### Redirect

Open `http://localhost:8080/{token}` in a browser.

### Metrics

GET `http://localhost:8080/api/v1/urls/{id}/metrics`
Header: `X-API-KEY: change-me`

## Structure

- `src/main/java`: backend code
- `src/main/resources`: configuration and Flyway migrations
- `docs/requirements`: requirements artifacts
- `docs/architecture`: architecture artifacts
- `openapi/openapi.yaml`: API contract
- `schemas/db_schema.md`: database schema

## Notes

The default API key is configured in `src/main/resources/application.yml`.
Change it for production use.
