Title: ADR 0001 — Database and Storage Strategy
Status: Proposed
Date: 2026-07-30
Authors: Architecture Agent (AI-assisted)

## Context
The assignment requires persistent storage for URL mappings and analytics events. The repository is currently empty, and the prototype needs to be runnable locally with a realistic migration path.

## Decision
- Use H2 as the default development and CI database.
- Support Postgres through a separate Spring profile and Flyway migrations.
- Define schema tables for `urls`, `events`, and `aggregates`.

## Consequences
- Pros: fast local iteration, simple CI, and production-compatible migration path.
- Cons: H2 can differ from Postgres in SQL semantics; mitigate with Postgres migration validation.

## Validation
- Integration tests against H2.
- Optional CI or local validation against a Postgres container.

## Related Decisions
- ADR 0002 — Token Generation Strategy
- ADR 0003 — Analytics Retention Policy

---

Artifact Contract
- Repository Location: docs/architecture/adr_0001_db_and_storage.md
- Purpose: Document database and storage strategy
- Created By: Architecture Agent (AI-assisted)
- Validation Status: Draft
- Recommended Commit: docs(architecture): add ADR 0001 (DB strategy)
