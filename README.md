# GeLibrary

A reactive REST API for library management built with **Spring WebFlux** and **MongoDB**, focused on non-blocking, asynchronous data streams using Project Reactor.

---

## Tech Stack

- **Java 17+**
- **Spring Boot**
- **Spring WebFlux** — reactive, non-blocking HTTP layer
- **Spring Data MongoDB Reactive** — async MongoDB integration
- **MongoDB** — document database
- **Maven** — build tool

---

## Architecture

```
src/main/java/com/WebfluxTest/Library/
├── controller/
│   └── BookController.java     # REST endpoints (WebFlux Router/Controller)
├── model/
│   ├── Book.java               # MongoDB document entity
│   ├── BookEvent.java          # SSE event model
│   └── SequenceCounter.java    # Auto-increment ID strategy
├── Repository/
│   └── LibraryRepository.java  # Reactive MongoDB repository
└── LibraryApplication.java     # Spring Boot entry point
```

---

## API Endpoints

| Method | Endpoint | Returns | Description |
|--------|----------|---------|-------------|
| `GET` | `/books` | `Flux<Book>` | List all books |
| `GET` | `/books/{id}` | `Mono<Book>` | Get book by ID |
| `POST` | `/books` | `Mono<Book>` | Create a single book |
| `POST` | `/books/bulk` | `Flux<Book>` | Create multiple books |
| `PUT` | `/books` | `Mono<Book>` | Update an existing book |
| `DELETE` | `/books/{id}` | `Mono<Void>` | Delete book by ID |
| `DELETE` | `/books` | `Mono<Void>` | Clear all books |
| `GET` | `/books/events` | `Flux<BookEvent>` | Stream events every 5s (SSE) |

---

## Key Concepts Demonstrated

- **Mono / Flux** — reactive types for single and multiple async values
- **flatMap** — chaining dependent async operations without blocking
- **defaultIfEmpty** — safe fallback for empty reactive streams
- **Server-Sent Events (SSE)** — real-time streaming via `text/event-stream`
- **Reactive MongoDB** — fully non-blocking database access
- **SequenceCounter pattern** — custom auto-increment ID generation in MongoDB

---

## Running Locally

**Prerequisites:** Java 17+, Maven, MongoDB running on `localhost:27017`

```bash
# Clone the repository
git clone https://github.com/Geborne/GeLibrary.git
cd GeLibrary

# Run the application
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

---

## Example Requests

```bash
# Get all books
curl http://localhost:8080/books

# Create a book
curl -X POST http://localhost:8080/books \
  -H "Content-Type: application/json" \
  -d '{"title": "Clean Code", "author": "Robert C. Martin", "rating": 5}'

# Stream events
curl -H "Accept: text/event-stream" http://localhost:8080/books/events
```

---

## Why Reactive?

Traditional Spring MVC uses one thread per request. Under high load (common in financial systems), threads block waiting for I/O — database queries, HTTP calls — wasting resources.

Spring WebFlux uses an event-loop model: a small number of threads handle thousands of concurrent requests by **never blocking**. This makes it well-suited for high-throughput, low-latency services.

---

## Roadmap

- [ ] Kotlin migration
- [ ] Docker + Docker Compose setup
- [ ] Unit and integration tests (JUnit 5 + StepVerifier)
- [ ] Jenkins CI/CD pipeline
- [ ] AWS deployment (ECS/EKS)
