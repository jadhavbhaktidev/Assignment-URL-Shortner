Artifact Name: Work Breakdown Structure (WBS)
Repository Location: execution/plans/wbs.md
Purpose: Decompose the URL Shortener project into epics, stories, tasks, dependencies, and estimates.
Created By: Task Decomposition Agent (AI-assisted)
Requirement Mapping: Maps to docs/requirements/requirement_analysis.md and docs/requirements/ambiguity_report.md
Validation Status: Draft
Dependencies: None
Recommended Commit: feat(planning): add WBS and tasks
Recommended Branch: feature/task-decomposition -> target: main

---

# Work Breakdown Structure (WBS)

## Epic 1 — Core URL Shortener
- Story 1.1: Create Short URL API
  - Task 1.1.1: Define API contract
  - Task 1.1.2: Implement controller/service/repository
  - Task 1.1.3: Implement token generation
  - Task 1.1.4: Add unit tests

- Story 1.2: Redirect Short URL
  - Task 1.2.1: Implement redirect endpoint
  - Task 1.2.2: Add cache layer
  - Task 1.2.3: Add integration tests

- Story 1.3: Custom Alias Support
  - Task 1.3.1: Alias validation
  - Task 1.3.2: Collision handling
  - Task 1.3.3: Alias availability endpoint

- Story 1.4: Management and Expiry
  - Task 1.4.1: Delete/expire endpoint
  - Task 1.4.2: Background expiry job

## Epic 2 — Analytics
- Story 2.1: Event Capture
  - Task 2.1.1: Define event schema
  - Task 2.1.2: Record events on redirect
  - Task 2.1.3: Unit tests for event capture

- Story 2.2: Aggregated Metrics
  - Task 2.2.1: Implement daily aggregates
  - Task 2.2.2: Metrics retrieval API
  - Task 2.2.3: Metrics correctness tests

## Epic 3 — Reliability & Operations
- Story 3.1: Health and Metrics
  - Task 3.1.1: Health-check endpoint
  - Task 3.1.2: Micrometer metrics exposure

- Story 3.2: Rate Limiting
  - Task 3.2.1: Implement rate limiter
  - Task 3.2.2: Rate limiting tests

- Story 3.3: Logging
  - Task 3.3.1: Structured logging setup

## Epic 4 — Security & Validation
- Story 4.1: Input Validation
  - Task 4.1.1: Long URL validation
  - Task 4.1.2: Alias validation rules

- Story 4.2: API Key Protection
  - Task 4.2.1: Secure management endpoints
  - Task 4.2.2: Auth tests

## Epic 5 — Persistence & DB
- Story 5.1: Database Schema and Migrations
  - Task 5.1.1: Design schema
  - Task 5.1.2: Add Flyway migrations
  - Task 5.1.3: Migration tests

## Epic 6 — Testing & CI
- Story 6.1: Unit Tests
  - Task 6.1.1: Coverage for services/controllers

- Story 6.2: Integration Tests
  - Task 6.2.1: End-to-end API tests

- Story 6.3: CI Workflow
  - Task 6.3.1: GitHub Actions build/test

## Epic 7 — Frontend (Minimal)
- Story 7.1: Basic Angular UI
  - Task 7.1.1: URL creation form
  - Task 7.1.2: Redirect verification

## Epic 8 — Documentation & Traceability
- Story 8.1: Documentation
  - Task 8.1.1: README and setup guide
  - Task 8.1.2: Architecture docs and ADRs
  - Task 8.1.3: AI traceability logs

# Dependency Graph
- DB migrations and schema design needed before backend implementation.
- Core API implementation is the critical path.
- Analytics depends on redirect implementation.
- Frontend depends on stable backend API.

# Prioritization
1. Schema and migrations
2. Shorten and redirect API
3. Validation and alias support
4. Analytics capture and metrics
5. Reliability and observability
6. Tests, CI, and docs
7. Minimal frontend
