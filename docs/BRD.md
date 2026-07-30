# Business Requirements Document (BRD)

## 1. Document Purpose
This BRD defines the business goals and required capabilities for the AI-assisted URL Shortener system implemented in this repository.

## 2. Business Context
Teams need a lightweight service to convert long URLs into compact links, track usage, and safely manage links. The solution must be fast to run locally, easy to validate, and clear enough for engineering review.

## 3. Business Goals
1. Reduce long-link complexity by generating short links.
2. Provide reliable redirection from short links to original URLs.
3. Expose usage insights (clicks and unique visitors).
4. Protect management operations (metrics and delete) with access control.
5. Provide an end-to-end prototype with runnable backend, frontend, and tests.

## 4. Stakeholders
- Product owner: validates feature outcomes and usability.
- Backend engineer: owns API correctness and persistence logic.
- Frontend engineer: owns UI flow for create, metrics, and delete.
- QA/reviewer: verifies behavior through tests and run instructions.

## 5. In-Scope Requirements
1. Create short URL with optional custom alias.
2. Redirect via token or alias.
3. Capture click events during redirect.
4. Return metrics by URL id (clicks, uniques).
5. Delete URL by id.
6. Enforce API key for management endpoints.
7. Provide setup, architecture, and validation documentation.

## 6. Out of Scope
1. Multi-tenant user accounts and role-based permissions.
2. Payment, billing, or quota plans.
3. Production-grade rate limiting and WAF rules.
4. Distributed cache and multi-region deployment.

## 7. Functional Requirements
1. URL Creation
- Input: longUrl (required), customAlias (optional).
- Behavior: validate alias format and uniqueness.
- Output: id, alias, shortUrl.

2. Redirection
- Input: token or alias path segment.
- Behavior: resolve mapping and return HTTP 302 with Location header.
- Failure: return 404 when mapping does not exist.

3. Analytics
- Capture event on each redirect.
- Increment cumulative clicks on URL mapping.
- Track unique visitors based on distinct hashed IP values.
- Metrics endpoint returns urlId, clicks, uniques.

4. Management
- Delete endpoint removes URL and associated dependent records.
- Metrics and delete require X-API-KEY.
- Unauthorized request returns 401.

## 8. Non-Functional Requirements
1. Local run support with Java 17+, Maven, Node.js.
2. Local persistence via H2 with Flyway migration.
3. Clear API contract and schema notes in repository docs.
4. Meaningful automated tests for core success and failure paths.

## 9. Success Criteria
1. Backend and frontend run locally and interact successfully.
2. Create, redirect, metrics, and delete flows work end to end.
3. Alias collision returns 409 and unknown token returns 404.
4. Build and test commands pass using documented instructions.

## 10. Traceability To Implementation
- API: openapi/openapi.yaml
- Backend source: src/main/java/com/aiassisted/urlshortener
- Frontend source: frontend/src/app
- Tests: src/test/java/com/aiassisted/urlshortener and frontend/src/app/app.component.spec.ts
- Setup guide: docs/setup_and_run.md
