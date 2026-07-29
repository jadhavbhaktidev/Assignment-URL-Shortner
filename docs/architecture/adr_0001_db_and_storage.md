Title: ADR 0001 — Database and Storage Strategy
Status: Proposed
Date: 2026-07-29
Authors: Architecture Agent (AI-assisted)

Context
-------
The assignment requires persistent storage for URL records and analytics events. The repo is initially empty; prototype must be runnable locally and be CI-friendly.

Decision
--------
- Use H2 (in-memory/file) as the default DB for development and CI tests (Spring profile `dev`).
- Provide a `postgres` Spring profile and Flyway migration scripts to support Postgres for production-like testing and deployments.
- Schema includes `urls` table, `events` table (raw clicks), and `aggregates` table (daily counters).

Consequences
------------
- Pros:
  - Fast local iteration with H2.
  - Portability to Postgres supported via Flyway migrations and JPA abstractions.
- Cons:
  - H2 behavior may differ from Postgres in some SQL edge cases — mitigated by integration tests against Postgres in CI when possible.

Rollback
--------
- Revert Postgres profile and use H2-only setup; remove Flyway if needed.

Validation
----------
- Integration tests run against H2.
- Optional CI job runs Flyway migrations on a Postgres container to validate compatibility.

Related Decisions
-----------------
- ADR 0002 — Token Generation Strategy
- ADR 0003 — Analytics Retention Policy

---

Artifact Contract
- Repository Location: docs/architecture/adr_0001_db_and_storage.md
- Purpose: Document DB choice and migration strategy
- Created By: Architecture Agent (AI-assisted)
- Validation Status: Draft — engineer sign-off required

Recommended Commit: docs(architecture): add ADR 0001 (DB strategy)
Recommended Branch: feature/architecture -> main
