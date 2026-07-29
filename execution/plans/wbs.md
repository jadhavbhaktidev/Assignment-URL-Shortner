Artifact Name: Work Breakdown Structure (WBS)
Repository Location: execution/plans/wbs.md
Purpose: Decompose the project into epics, stories, tasks, dependencies and estimates to guide implementation.
Created By: Task Decomposition Agent (AI-assisted)
Requirement Mapping: Maps to docs/requirements/requirement_analysis.md and docs/requirements/ambiguity_report.md
Validation Status: Draft
Dependencies: None
Recommended Commit: feat(planning): add WBS and tasks
Recommended Branch: feature/task-decomposition -> target: main

---

# Work Breakdown Structure (WBS)

## Epic 1 — Core URL Shortener
- Story 1.1: API: Create Short URL
  - Task 1.1.1: Define API contract (request/response) — 1d
  - Task 1.1.2: Implement controller, service, repository — 2d
  - Task 1.1.3: Token generation (collision-safe) — 1d
  - Task 1.1.4: Unit tests for service + controller — 1d
  - Dependencies: 1.1.1 → 1.1.2 → 1.1.3 → 1.1.4

- Story 1.2: API: Redirect Short URL
  - Task 1.2.1: Implement redirect controller — 1d
  - Task 1.2.2: Add cache layer for hot tokens (Caffeine) — 1d
  - Task 1.2.3: Integration tests (create->redirect) — 1d
  - Dependencies: 1.1.2 → 1.2.1

- Story 1.3: Custom Alias Support
  - Task 1.3.1: Alias validation rules + endpoint — 0.5d
  - Task 1.3.2: Collision handling and 409 responses — 0.5d
  - Tests: alias validation + collision test — 0.5d
  - Dependencies: 1.1.2

- Story 1.4: Management (Delete/Expire)
  - Tasks: implement delete/expire endpoints, background expiry job — 1d
  - Dependencies: 1.1.2

## Epic 2 — Analytics
- Story 2.1: Event Capture
  - Task 2.1.1: Define analytics event schema — 0.5d
  - Task 2.1.2: Implement event recording on redirect — 1d
  - Task 2.1.3: Unit tests for event capture — 0.5d
  - Dependencies: 1.2.1

- Story 2.2: Aggregation & Metrics
  - Task 2.2.1: Implement aggregated counters (daily) — 1d
  - Task 2.2.2: API for metrics retrieval — 0.5d
  - Tests: metrics correctness tests — 0.5d
  - Dependencies: 2.1.1 → 2.2.1

## Epic 3 — Reliability & Operations
- Story 3.1: Health & Metrics
  - Task 3.1.1: Health-check endpoint — 0.25d
  - Task 3.1.2: Basic Prometheus metrics exposition (Micrometer) — 0.5d

- Story 3.2: Rate Limiting & Abuse Controls
  - Task 3.2.1: Implement per-IP or per-key rate limiter (Bucket4j) — 1d
  - Tests: rate-limiter tests — 0.5d

- Story 3.3: Logging & Tracing
  - Task 3.3.1: Structured logging setup (Logback) — 0.5d
  - Task 3.3.2: Optional tracing hooks (OpenTelemetry stub) — 0.5d

## Epic 4 — Security & Validation
- Story 4.1: Input Validation
  - Task 4.1.1: Validate long URLs (no open-redirect) — 0.5d
  - Task 4.1.2: Alias character set validation — 0.25d

- Story 4.2: Management Auth
  - Task 4.2.1: Simple API key enforcement for management endpoints — 0.5d
  - Tests: auth tests — 0.5d

## Epic 5 — Persistence & DB
- Story 5.1: Schema & Migrations
  - Task 5.1.1: Design DB schema (urls, events, aggregates) — 1d
  - Task 5.1.2: Flyway migration scripts + H2 profile — 0.5d
  - Tests: migration verification — 0.5d

## Epic 6 — Tests & CI
- Story 6.1: Unit Tests
  - Task 6.1.1: Add unit tests to reach baseline coverage — 1d
- Story 6.2: Integration Tests
  - Task 6.2.1: Wiremock / Testcontainers integration tests for DB — 1d
- Story 6.3: CI Pipeline
  - Task 6.3.1: Create simple GitHub Actions workflow (build, test) — 0.5d

## Epic 7 — Frontend (Angular) - Minimal MVP
- Story 7.1: Basic UI
  - Task 7.1.1: Simple create-short-url form and result display — 1d
  - Task 7.1.2: Redirect flow test using browser — 0.5d
  - Dependencies: 1.1.2

## Epic 8 — Documentation & Traceability
- Story 8.1: Docs
  - Task 8.1.1: README + setup guide — 0.5d
  - Task 8.1.2: Architecture overview & ADRs — 1d
  - Task 8.1.3: AI traceability logs under `ai/` — 0.5d

---

# Dependency Graph (high-level)
- Database schema (5.1) needed early for many back-end tasks.
- Core API implementation (1.1, 1.2) is the critical path.
- Analytics depends on redirect implementation.
- Frontend is dependent on core APIs being available.

# Prioritization & Execution Order (MVP-focused)
1. 5.1 Schema & migrations
2. 1.1 Create endpoint + 1.2 Redirect endpoint
3. 1.3 Custom alias + 4.1 Validation
4. 2.1 Event capture + 2.2 Aggregation
5. 3.1 Health, metrics, logging
6. 6.3 CI pipeline and tests
7. 7.1 Minimal Angular frontend
8. 8.1 Docs & traceability

# Estimates Summary (approx)
- Total dev effort (backend MVP core + analytics + tests): ~8–12 days (single developer estimate for prototype)
- With frontend + CI + docs: ~10–14 days

# Owners
- Owner placeholders: `@engineer` for tasks until assignments are known.

# Validation Strategy
- Preconditions: ambiguous decisions must be signed off.
- Unit and integration tests per story.
- CI run for build and test on PRs.

# Commit & PR Recommendations
- Break work into small feature branches per epic (e.g., `feature/core-url-shortener`).
- Commit messages follow Conventional format defined in the assignment.
- Open PR per epic with linked issues and traceability notes.

---

Please review and approve or request edits; upon approval I will create the milestone plan and generate issues/tasks if you want them represented as tracker-ready artifacts.
