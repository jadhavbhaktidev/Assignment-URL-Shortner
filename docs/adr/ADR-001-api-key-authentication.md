# ADR-001: API Key Authentication for Protected Endpoints

## Status
Accepted (prototype scope)

## Context
The service needs lightweight access control for metrics and delete endpoints while keeping implementation simple for local evaluation.

## Decision
Use header-based API key (`X-API-KEY`) for `/api/v1/urls/**` endpoints.

## Alternatives Considered
1. JWT with RBAC.
2. OAuth2/OIDC integration.
3. Session-based auth.

## Consequences
- Positive: very low implementation complexity, easy local testing.
- Negative: coarse-grained auth and shared secret risk.

## Follow-up Actions
- Replace with JWT/OAuth2 for production.
- Introduce scoped roles for metrics vs delete actions.
