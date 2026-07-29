Artifact Name: API Contract Summary
Repository Location: docs/api/api_contract.md
Purpose: Human-readable summary of the API endpoints and usage.
Created By: API & Schema Agent (AI-assisted)
Validation Status: Draft

---

# API Contract (summary)

Base path: `/api/v1`

1. POST /api/v1/shorten
- Description: Create a short URL for a given `longUrl` and optional `customAlias`.
- Request body:
  - `longUrl` (string, required)
  - `customAlias` (string, optional)
- Responses:
  - 201 Created: `{ id, shortUrl, alias }`
  - 400 Bad Request: validation error
  - 409 Conflict: alias collision

2. GET /{token}
- Description: Redirect to the original `longUrl`.
- Path param: `token` (string)
- Responses: 302 Redirect on success, 404 Not Found otherwise

3. GET /api/v1/urls/{id}/metrics (protected)
- Description: Retrieve analytics for a URL (aggregated counts, trends)
- Security: `X-API-KEY` header required for management endpoints

4. DELETE /api/v1/urls/{id} (protected)
- Description: Delete or expire a short URL
- Security: `X-API-KEY`
- Responses: 204 No Content on success

# Authentication
- Management endpoints require `X-API-KEY` header. Public endpoints (creating and redirecting) are open but rate-limited.

# Error Handling
- Use standard HTTP status codes and return JSON error bodies with `code` and `message` fields.

# Rate Limiting
- Implement per-IP or per-API-key rate limiting; return `429 Too Many Requests` with `Retry-After` header when throttled.

---

Recommended Commit: feat(api): add API contract summary
