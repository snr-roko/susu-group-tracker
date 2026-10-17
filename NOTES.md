# Notes - Assumptions & Decisions

## Assumptions
- The fixed weekly contribution lives on each collection period (intentional, not on the group).
- Coordinator manually passes the beneficiary when starting a week (auto-rotation deferred).

## Decisions made
- Money is handled with `BigDecimal` throughout (no rounding errors).
- Reject zero or negative payment amounts in `payCollection`.
- Reject duplicate member names within a group in `addMember` (clean check before the DB unique constraint).
- When a week cannot close, `endCollectionWeek` throws an exception naming each member who still owes and how much (shortfall = weekly amount minus amount paid).
- Payout is represented by the accumulated beneficiary balance (deposits credited during `payCollection`); no separate lump-sum payout entity was added.
- Tests run against embedded H2 (`src/test/resources/application.properties`); production keeps PostgreSQL.

## Improvements with more time
- Auto-rotate the beneficiary to the next member in order when a week closes.
- Add an `active` field on members and enforce back-payment for missed weeks before activation for members midway.
- Add a dedicated payout/pot history entity and report endpoint.
- Add REST controllers and a real API instead of service-layer-only code.
- Add input validation (e.g. `@Valid`, non-blank names, positive amounts at DTO level).
- Add more edge-case tests.
