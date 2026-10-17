# Susu Group Tracker

Minimal Spring Boot service for tracking Susu (rotating savings) group contributions.

## Build & run

Requires Java 21 and Maven (wrapper included).

Run the app:
```
./mvnw spring-boot:run
```
The app connects to PostgreSQL per `src/main/resources/application.yaml` (`jdbc:postgresql://localhost:5432/susu_group_tracker_db`).

Run the tests (embedded H2):
```
./mvnw test
```

## Notes

See `NOTES.md` for assumptions and decisions.
