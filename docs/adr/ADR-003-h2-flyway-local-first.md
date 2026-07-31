# ADR-003: H2 and Flyway for Local-First Development

## Status
Accepted (prototype scope)

## Context
The assignment prioritizes rapid local setup and deterministic schema evolution.

## Decision
Use H2 in-memory DB for development/testing and Flyway for versioned schema migration.

## Alternatives Considered
1. Postgres for all local/dev/test environments.
2. No migration framework.
3. Embedded file-based persistence.

## Consequences
- Positive: fast startup, reproducible schema state, easy onboarding.
- Negative: in-memory behavior differs from production-grade persistent stores.

## Follow-up Actions
- Add Postgres integration profile and CI validation.
- Add migration checks against target production DB engine.
