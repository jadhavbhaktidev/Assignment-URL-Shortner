# AI-Assisted URL Shortener

## Overview
This repository contains a working URL shortener prototype with:
- Spring Boot backend API
- H2 + Flyway persistence
- Angular frontend for end-to-end interaction
- Integration and frontend unit tests

Core capabilities:
- Create short URL with optional custom alias
- Redirect from token/alias to long URL
- API key-protected metrics and delete endpoints
- Click and unique-visitor analytics capture

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.9+
- Node.js 18+

### 1) Run backend
```bash
mvn spring-boot:run
```
Backend URL: `http://localhost:8080`

### 2) Run frontend
```bash
cd frontend
npm install
npm start
```
Frontend URL: `http://localhost:4200`

### 3) Use API key for protected endpoints
Default key: `change-me`

Configured in:
- `src/main/resources/application.yml`

## Validation Commands

Backend tests:
```bash
mvn test
```

Frontend tests:
```bash
cd frontend
npm test -- --watch=false --browsers=ChromeHeadless
```

Frontend production build:
```bash
cd frontend
npm run build
```

## API and Design Artifacts
- API contract: `openapi/openapi.yaml`
- DB schema notes: `schemas/db_schema.md`
- Assignment compliance package: `docs/assignment1/README.md`

## Project Structure
- `src/main/java`: backend code
- `src/main/resources`: config and Flyway migration
- `src/test/java`: backend integration tests
- `frontend/`: Angular app and tests
- `docs/assignment1/`: Assignment 1 audit and compliance documentation
