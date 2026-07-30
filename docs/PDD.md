# Product Design Document (PDD)

## 1. Product Overview
The product is a minimal URL shortener with a backend API and Angular UI. It supports creation of short links, usage analytics lookup, and URL deletion with API key protection.

## 2. User Personas
1. Operator/Developer
- Needs to generate short links quickly.
- Needs to verify redirects and inspect click metrics.
- Needs to delete invalid or expired links.

2. Public Visitor
- Opens short links and should be redirected to destination URL reliably.

## 3. Primary User Journeys
1. Create short URL
- User enters long URL and optional alias in UI.
- UI calls POST /api/v1/shorten.
- Response displays generated short URL and URL id.

2. Verify metrics
- User enters URL id and API key.
- UI calls GET /api/v1/urls/{id}/metrics.
- UI displays clicks and uniques.

3. Delete URL
- User enters URL id and API key.
- UI calls DELETE /api/v1/urls/{id}.
- UI displays success or failure message.

4. Redirect usage
- Visitor opens short URL.
- Backend resolves token/alias and returns 302 redirect.
- Analytics event is recorded.

## 4. Product Requirements
1. Usability
- Single-page interface with clear forms for create, metrics, and delete.
- Actionable error messages for invalid API key or missing id.

2. Integrity
- Alias collisions must be blocked with 409 response.
- Unknown short URLs must return 404.

3. Security
- Management and metrics are protected by X-API-KEY.
- Redirect analytics store hashed IP values for privacy.

4. Observability
- Health endpoint available through Spring Actuator.
- Validation evidence provided by backend and frontend test runs.

## 5. UX and Interaction Notes
- UI defaults API key to change-me for local demo.
- Metrics and delete controls are separated from create form.
- Result panel returns short URL and generated id for immediate follow-up actions.

## 6. Constraints and Trade-offs
1. Prototype authentication uses shared API key, not user accounts.
2. H2 in-memory storage resets on backend restart.
3. Unique visitor logic is approximated via distinct hashed IP values.

## 7. Acceptance Criteria
1. User can create a short URL and receive id + short link.
2. Opening short link redirects to expected destination.
3. Metrics endpoint returns non-zero click values after redirects.
4. Unauthorized metrics/delete calls return 401.
5. Deleting existing URL returns 204, repeated delete returns 404.

## 8. Product Artifacts
- UI implementation: frontend/src/app/app.component.ts
- UI template/styles: frontend/src/app/app.component.html and frontend/src/app/app.component.css
- API contract: openapi/openapi.yaml
- Validation references: src/test/java/com/aiassisted/urlshortener and frontend/src/app/app.component.spec.ts
