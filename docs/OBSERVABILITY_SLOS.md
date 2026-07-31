# Observability, SLIs, and SLOs

## Purpose
Define how the URL shortener should be observed in production, what service indicators are measured, and which alert thresholds are used.

## Existing Instrumentation
- Request correlation: `X-Request-ID` response header and MDC logging context.
- Actuator endpoints exposed: `health`, `info`, `metrics`.
- Error payloads include request IDs for traceability.

## Service Level Indicators (SLIs)
1. Availability SLI
- Definition: successful API responses over total requests.
- Scope: `POST /api/v1/shorten`, `GET /{token}`, `GET /api/v1/urls/{id}/metrics`, `DELETE /api/v1/urls/{id}`.
- Formula: `1 - (5xx responses / total requests)`.

2. Redirect Success SLI
- Definition: successful redirect responses over redirect requests.
- Scope: `GET /{token}`.
- Formula: `302 responses / total redirect requests`.

3. Latency SLI
- Definition: p95 latency for core endpoints.
- Scope: shorten, redirect, metrics, delete.

4. Rate-Limit SLI
- Definition: ratio of `429` responses on public endpoints.
- Scope: `POST /api/v1/shorten`, `GET /{token}`.

## Service Level Objectives (SLOs)
1. Availability SLO
- Target: 99.9% monthly availability.

2. Redirect Success SLO
- Target: 99.95% of redirect requests return `302` or controlled `404` (no 5xx).

3. Latency SLO
- Target: p95 < 300ms for shorten and redirect, p95 < 400ms for metrics/delete.

4. Error Budget
- Monthly error budget for availability: 0.1%.
- Policy: pause risky releases if burn rate exceeds 2x for 1 hour.

## Alert Policy
1. Critical alerts
- Availability below 99.5% over rolling 15 minutes.
- 5xx error rate above 2% for 10 minutes.

2. Warning alerts
- p95 latency above SLO for 15 minutes.
- Unexpected sustained 429 spikes above baseline.

## Dashboard Minimums
- Request volume by endpoint and status code class.
- p50/p95/p99 latency by endpoint.
- 5xx and 4xx trends.
- Rate-limit events.
- Health endpoint status timeline.

## Ownership
- Primary owner: backend engineer.
- Secondary owner: on-call reviewer.

## Review Cadence
- Weekly review of SLI/SLO adherence.
- Monthly threshold and error-budget policy review.
