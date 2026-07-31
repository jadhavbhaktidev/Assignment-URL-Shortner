# ADR-002: In-Memory Rate Limiting for Public Endpoints

## Status
Accepted (prototype scope)

## Context
Public endpoints require abuse protection quickly without external infrastructure.

## Decision
Use an in-memory per-client rate limiter for `POST /api/v1/shorten` and `GET /{token}`.

## Alternatives Considered
1. Redis-backed distributed rate limiting.
2. API gateway/native ingress rate limiting.
3. External WAF controls.

## Consequences
- Positive: no external dependency, straightforward implementation.
- Negative: not distributed across nodes and reset on restart.

## Follow-up Actions
- Move to distributed rate limiting for multi-instance deployments.
- Add environment-specific thresholds and alerting.
