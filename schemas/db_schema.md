Artifact Name: Database Schema
Repository Location: schemas/db_schema.md
Purpose: Define database tables and indices for the URL Shortener prototype.
Created By: API & Schema Agent (AI-assisted)
Validation Status: Draft

---

# Schema Overview

Tables:

1. `urls`
   - Purpose: store mapping between token/alias and long URL.
   - Columns:
     - `id` BIGSERIAL PRIMARY KEY
     - `token` VARCHAR(128) NOT NULL UNIQUE -- auto-generated short token
     - `alias` VARCHAR(100) UNIQUE NULL -- optional custom alias
     - `long_url` TEXT NOT NULL
     - `created_at` TIMESTAMP WITH TIME ZONE DEFAULT now()
     - `expires_at` TIMESTAMP WITH TIME ZONE NULL
     - `api_key_owner` VARCHAR(256) NULL -- optional owner identifier
     - `clicks_count` BIGINT DEFAULT 0 -- denormalized counter for quick access
   - Indexes: `idx_urls_token`, `idx_urls_alias`

2. `events`
   - Purpose: raw click events for analytics
   - Columns:
     - `id` BIGSERIAL PRIMARY KEY
     - `url_id` BIGINT NOT NULL REFERENCES urls(id) ON DELETE CASCADE
     - `occurred_at` TIMESTAMP WITH TIME ZONE DEFAULT now()
     - `ip_hash` VARCHAR(128) NULL -- store hashed IP to reduce PII risk
     - `user_agent` TEXT NULL
     - `referrer` TEXT NULL
     - `country` VARCHAR(64) NULL
   - Indexes: `idx_events_url_id`, `idx_events_occurred_at`

3. `aggregates`
   - Purpose: daily aggregated counters per URL
   - Columns:
     - `id` BIGSERIAL PRIMARY KEY
     - `url_id` BIGINT NOT NULL REFERENCES urls(id) ON DELETE CASCADE
     - `day` DATE NOT NULL
     - `clicks` BIGINT DEFAULT 0
     - `uniques` BIGINT DEFAULT 0
   - Unique constraint: (`url_id`, `day`)

# Notes
- Use Flyway for schema migrations; initial migration provided in `src/main/resources/db/migration/V1__init.sql`.
- For prototype, H2 profile will use compatible types but Postgres is target for production.
- PII: avoid storing plaintext IP addresses; use hashing or truncation.

# Validation
- Integration tests should verify schema creation and constraints.

---

Recommended Commit: feat(schema): add DB schema definition
