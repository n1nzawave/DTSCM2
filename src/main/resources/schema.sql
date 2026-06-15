CREATE TABLE IF NOT EXISTS tasks(
    id BIGSERIAL PRIMARY KEY,
    url TEXT,
    interval_seconds FLOAT(24),
    status BOOLEAN,
    last_checked_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS task_log(
    id BIGSERIAL PRIMARY KEY,
    task_id BIGINT REFERENCES tasks(id) ON DELETE CASCADE,
    timestamp TIMESTAMP,
    status_code INTEGER,
    latency_ms BIGINT,
    is_success BOOLEAN
);