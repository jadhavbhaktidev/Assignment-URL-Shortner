Artifact Name: Database Schema
Repository Location: schemas/db_schema.md
Purpose: Describe database tables and indices for the URL Shortener prototype.
Created By: API & Schema Agent (AI-assisted)
Validation Status: Draft

---

# Schema Overview

## urls
- id: BIGSERIAL PRIMARY KEY
- token: VARCHAR(128) NOT NULL UNIQUE
- alias: VARCHAR(100) UNIQUE NULL
- long_url: TEXT NOT NULL
- created_at: TIMESTAMPTZ DEFAULT now()
- expires_at: TIMESTAMPTZ NULL
- api_key_owner: VARCHAR(256) NULL
- clicks_count: BIGINT DEFAULT 0

Indexes:
- idx_urls_token on token
- idx_urls_alias on alias

## events
- id: BIGSERIAL PRIMARY KEY
- url_id: BIGINT NOT NULL REFERENCES urls(id) ON DELETE CASCADE
- occurred_at: TIMESTAMPTZ DEFAULT now()
- ip_hash: VARCHAR(128) NULL
- user_agent: TEXT NULL
- referrer: TEXT NULL
- country: VARCHAR(64) NULL

Indexes:
- idx_events_url_id on url_id
- idx_events_occurred_at on occurred_at

## aggregates
- id: BIGSERIAL PRIMARY KEY
- url_id: BIGINT NOT NULL REFERENCES urls(id) ON DELETE CASCADE
- day: DATE NOT NULL
- clicks: BIGINT DEFAULT 0
- uniques: BIGINT DEFAULT 0

Constraints:
- UNIQUE (url_id, day)

Indexes:
- idx_aggregates_url_day on (url_id, day)

# Notes
- Flyway migrations will be used for schema creation.
- H2 will be used for local dev, Postgres is the target production DB.
- Avoid storing plaintext IP addresses; use hashing.
