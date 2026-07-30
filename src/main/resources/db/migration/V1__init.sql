-- Flyway V1: initial schema for URL shortener

CREATE TABLE urls (
  id BIGSERIAL PRIMARY KEY,
  token VARCHAR(128) NOT NULL UNIQUE,
  alias VARCHAR(100) UNIQUE,
  long_url TEXT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  expires_at TIMESTAMP WITH TIME ZONE,
  api_key_owner VARCHAR(256),
  clicks_count BIGINT DEFAULT 0
);

CREATE INDEX idx_urls_token ON urls(token);
CREATE INDEX idx_urls_alias ON urls(alias);

CREATE TABLE events (
  id BIGSERIAL PRIMARY KEY,
  url_id BIGINT NOT NULL REFERENCES urls(id) ON DELETE CASCADE,
  occurred_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  ip_hash VARCHAR(128),
  user_agent TEXT,
  referrer TEXT,
  country VARCHAR(64)
);

CREATE INDEX idx_events_url_id ON events(url_id);
CREATE INDEX idx_events_occurred_at ON events(occurred_at);

CREATE TABLE aggregates (
  id BIGSERIAL PRIMARY KEY,
  url_id BIGINT NOT NULL REFERENCES urls(id) ON DELETE CASCADE,
  aggregate_day DATE NOT NULL,
  clicks BIGINT DEFAULT 0,
  uniques BIGINT DEFAULT 0,
  UNIQUE (url_id, aggregate_day)
);

CREATE INDEX idx_aggregates_url_day ON aggregates(url_id, aggregate_day);
