# Incident Response Runbook

## Purpose
Provide a standard operating procedure for diagnosing and mitigating production incidents for the URL shortener service.

## Severity Levels
- Sev 1: complete outage or widespread 5xx errors.
- Sev 2: partial outage, major latency regression, or broken protected operations.
- Sev 3: degraded non-critical function with available workaround.

## Initial Triage (First 10 Minutes)
1. Confirm impact
- Check `/actuator/health` and endpoint-level error rates.
- Determine affected user flows (create, redirect, metrics, delete).

2. Gather correlation context
- Capture `X-Request-ID` values from failing requests.
- Search logs by request ID and timestamp.

3. Classify severity
- Assign Sev level and declare incident channel.

## Common Failure Modes and Immediate Actions
1. High 5xx rate
- Validate DB connectivity and Flyway state.
- Roll back recent deployment if correlated with release.

2. Elevated latency
- Check resource saturation and thread pool pressure.
- Apply temporary traffic shaping or reduce non-essential load.

3. Authentication failures (`401` spike)
- Validate `URLSHORTENER_API_KEY` configuration.
- Verify no accidental config drift across environments.

4. Rate-limit false positives (`429` spike)
- Inspect request origin patterns and burst behavior.
- Temporarily tune `urlshortener.rate-limit.max-requests` if needed.

## Communication Protocol
1. Incident start message
- Include severity, start time, blast radius, and current mitigation.

2. Update cadence
- Sev 1: every 15 minutes.
- Sev 2: every 30 minutes.
- Sev 3: hourly.

3. Resolution message
- Include root cause, mitigation, and customer impact summary.

## Recovery and Verification
1. Confirm health and endpoint behavior:
- `POST /api/v1/shorten`
- `GET /{token}`
- `GET /api/v1/urls/{id}/metrics`
- `DELETE /api/v1/urls/{id}`

2. Verify no ongoing 5xx or severe latency regression.

3. Ensure request ID correlation still present in responses and logs.

## Post-Incident Review (Within 48 Hours)
1. Root cause analysis with timeline.
2. Corrective and preventive actions with owners.
3. Backlog updates for tests, monitors, or architecture hardening.
4. Documentation updates to this runbook and SLOs.
