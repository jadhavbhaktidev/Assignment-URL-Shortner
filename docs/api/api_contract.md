Artifact Name: API Contract Summary
Repository Location: docs/api/api_contract.md
Purpose: Provide a human-readable summary of the URL Shortener API endpoints.
Created By: API & Schema Agent (AI-assisted)
Validation Status: Draft

---

# API Contract Summary

Base path: `/api/v1`

## POST /api/v1/shorten
- Creates a short URL from a long URL.
- Request body:
  - `longUrl` (string, required)
  - `customAlias` (string, optional)
- Responses:
  - `201 Created`: returns `id`, `shortUrl`, and `alias`
  - `400 Bad Request`: validation error
  - `409 Conflict`: alias collision

## GET /{token}
- Redirects to the original URL.
- Path parameter: `token`
- Responses:
  - `302 Found` with `Location` header
  - `404 Not Found`

## GET /api/v1/urls/{id}/metrics
- Returns analytics for a URL.
- Protected via `X-API-KEY`.
- Response: aggregated metrics data.

## DELETE /api/v1/urls/{id}
- Deletes or expires a short URL.
- Protected via `X-API-KEY`.
- Responses:
  - `204 No Content` on success
  - `404 Not Found`

## Authentication
- `X-API-KEY` is required for management/analytics endpoints.
- Public endpoints remain accessible but should be rate-limited.

## Error Handling
- Use JSON error responses with `code` and `message`.
- `429 Too Many Requests` for rate limiting.
