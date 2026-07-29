Artifact Name: Milestone Plan
Repository Location: execution/plans/milestone_plan.md
Purpose: Define milestones, dates (relative), acceptance criteria, and exit criteria for each milestone.
Created By: Task Decomposition Agent (AI-assisted)
Requirement Mapping: Maps to execution/plans/wbs.md and docs/requirements/
Validation Status: Draft
Dependencies: wbs.md
Recommended Commit: feat(planning): add milestone plan
Recommended Branch: feature/task-decomposition -> target: main

---

# Milestone Plan (Sprint-style, relative days)

Note: These are recommended sprint-length milestones for a single developer prototype. Adjust team sizing/dates accordingly.

## Milestone 1 — Project Setup & Core Schema (Day 0–1)
- Goals:
  - Initialize Maven/Gradle project structure
  - Create `application.yml` profiles for H2 and Postgres
  - Add Flyway and initial migrations for `urls`, `events`, `aggregates` tables
- Acceptance Criteria:
  - Application boots with H2 profile
  - Migrations applied successfully
- Exit Criteria:
  - CI job runs ‘build’ and passes with H2

## Milestone 2 — Core Shorten & Redirect APIs (Day 1–3)
- Goals:
  - Implement create-short-url API and redirect endpoint
  - Token generation and persistence
  - Basic unit and integration tests
- Acceptance Criteria:
  - Create + redirect end-to-end tests pass
  - Redirect uses cache for repeated requests

## Milestone 3 — Alias, Validation & Security (Day 3–4)
- Goals:
  - Implement custom alias support
  - Input validation to prevent open redirects
  - Simple API key protection for management endpoints
- Acceptance Criteria:
  - Alias collision tests
  - Management endpoints require `X-API-KEY` header for protected actions

## Milestone 4 — Analytics Capture & Aggregation (Day 4–6)
- Goals:
  - Record click events on redirect
  - Provide aggregated metrics API
  - Implement data retention policy (30 days) job
- Acceptance Criteria:
  - Metrics API returns correct totals for sample data

## Milestone 5 — Reliability, Rate-Limiting & Observability (Day 6–7)
- Goals:
  - Add health endpoint, basic metrics (Micrometer)
  - Implement rate-limiting middleware
  - Structured logs
- Acceptance Criteria:
  - Health endpoint returns OK
  - Rate-limiter blocks excessive requests in tests

## Milestone 6 — Tests, CI & Documentation (Day 7–9)
- Goals:
  - Expand test coverage to include integration tests
  - Add GitHub Actions workflow for build & tests
  - Draft README and setup instructions
- Acceptance Criteria:
  - CI passes on PR with tests
  - README includes local dev steps

## Milestone 7 — Minimal Frontend & Final Report (Day 9–11)
- Goals:
  - Add simple Angular UI for create + view
  - Produce final engineering summary and traceability artifacts
- Acceptance Criteria:
  - Frontend can create a short URL and redirect via browser
  - Final report drafted with AI traceability logs

---

# Risk & Mitigation per Milestone
- M1: Misconfigured migrations — Mitigation: include test that runs Flyway.
- M2: Token collisions — Mitigation: deterministic collision checks + retry.
- M4: Analytics volume — Mitigation: store aggregated counters and cap raw events in prototype.

# Branching & PR Strategy
- Create branch per milestone/epic: e.g., `feature/core-shortener` for Milestone 2.
- Small PRs with linked docs and traceability notes.

# Next Steps
- Create issues or task cards from the WBS (optional).
- Start implementation on `feature/core-url-shortener` branch.

---

Validation: Draft — requires engineer approval to proceed with implementation tasks.
