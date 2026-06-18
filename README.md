# DTSCM2 — Distributed Task Status Checking Monitor

A website uptime monitoring service built with Spring Boot, RabbitMQ, and PostgreSQL. Registers URLs, checks them on a schedule, logs response metrics, and sends Telegram alerts when something goes down.

---

## How it works

You register a URL via the REST API and specify how often it should be checked. A scheduler runs every second, picks tasks whose interval has elapsed, and pushes them into a RabbitMQ queue. A worker consumes those messages, makes an HTTP GET request to the target URL, measures the response time, and saves the result to the database. If the request fails — non-2xx status or an exception — a Telegram message is sent with the status code and latency.

```
Scheduler --> RabbitMQ --> TaskWorker --> PostgreSQL
                                    \--> Telegram (on failure)
```

---

## Tech stack

- Java 17
- Spring Boot 4.1
- RabbitMQ 4
- PostgreSQL 17
- Spring Data JPA
- Docker / Docker Compose
- Maven

---

## Running locally

**Requirements:** Docker and Docker Compose.

Clone the repo and create a `.env` file in the project root:

```
POSTGRES_DB=dtscm2
POSTGRES_USER=admin
POSTGRES_PASSWORD=your_password
TELEGRAM_BOT_TOKEN=your_telegram_bot_token
```

The bot token is read from the environment at startup (`spring.telegram.bot.token` in `application.yml`) — no need to edit any source files.

> **Note:** the Telegram chat ID that receives alerts is currently hardcoded in `TaskWorker.java`. Update it there to your own chat ID before running.

Then start everything:

```bash
docker compose up --build
```

This brings up three containers: PostgreSQL, RabbitMQ, and the application itself. The API will be available at `http://localhost:8080`.

---

## API

### Register a URL

```
POST /api/tasks
Content-Type: application/json

{
  "url": "https://example.com",
  "intervalSeconds": 30,
  "status": true
}
```

### Get all tasks

```
GET /api/tasks
```

---

## Database schema

```sql
CREATE TABLE tasks (
    id               BIGSERIAL PRIMARY KEY,
    url              TEXT,
    interval_seconds FLOAT(24),
    status           BOOLEAN,
    last_checked_at  TIMESTAMP
);

CREATE TABLE task_log (
    id          BIGSERIAL PRIMARY KEY,
    task_id     BIGINT REFERENCES tasks(id) ON DELETE CASCADE,
    timestamp   TIMESTAMP,
    status_code INTEGER,
    latency_ms  BIGINT,
    is_success  BOOLEAN
);
```

Schema is created automatically on startup via `schema.sql` (`spring.sql.init.mode: always`), and validated against the JPA entities (`ddl-auto: validate`).

---

## RabbitMQ management UI

Available at `http://localhost:15672`. Default credentials: `admin` / `password`.

---

## Roadmap / known limitations

- Telegram chat ID is hardcoded — should be moved to an environment variable alongside the bot token.
- No input validation on `POST /api/tasks` (e.g. negative intervals, malformed URLs are accepted as-is).
- No web dashboard yet — tasks and metrics can currently only be inspected via the REST API or directly in the database.
- No retry/backoff or dead-letter queue if a worker fails to process a message.
