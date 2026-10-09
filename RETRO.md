# Sprint Retrospectives (RETRO.md)

Format: One line: what worked, what didn't, one change for the next sprint. (Section 16 Point 2)

## Sprint 1 (Jul 24 – Aug 9: Problem Statement, Auth & Base MVP)
- **What worked:** Fast schema setup with H2 and HikariCP connection pooling enabled rapid local testing.
- **What didn't:** Manual password hashing led to minor inconsistencies in early tests.
- **One change for next sprint:** Standardized on `PasswordUtil` with jBCrypt work factor 10 across all layers.

## Sprint 2 (Aug 10 – Aug 23: Seller Central & Admin Console)
- **What worked:** Clean DAO interface separation allowed simultaneous development of seller product CRUD and admin moderation.
- **What didn't:** Handling stock decrements outside transactions caused temporary race condition risks.
- **One change for next sprint:** Wrapped order placement and stock decrement inside atomic database transactions (`conn.setAutoCommit(false)`).

## Sprint 3 (Aug 24 – Sep 6: Search, Filters & Reviews Workflow)
- **What worked:** Dynamic PreparedStatement query building made multi-attribute search fast and secure against SQL injection.
- **What didn't:** Review calculation required a double query for average and count.
- **One change for next sprint:** Automated product review count and average rating updates directly in `ProductDAOImpl.updateRatingAndReviewCount`.

## Sprint 4 (Sep 7 – Sep 20: Security Hardening & Payment Strategy)
- **What worked:** Strategy pattern for mock payment simulation made UPI, Card, and COD swappable without altering order orchestration.
- **What didn't:** Session fixation vulnerability needed explicit handling.
- **One change for next sprint:** Enforced `session.invalidate()` and `request.getSession(true)` on every successful login.

## Sprint 5 (Sep 21 – Oct 10: AI Chatbot, Observability & Polish)
- **What worked:** Decoupling `ChatProvider` interface allowed fallback mock intelligence with 10+ FAQ responses even without network access.
- **What didn't:** Repeated chat queries caused unnecessary processor cycles.
- **One change for next sprint:** Implemented per-session in-memory response cache and sliding-window rate limiting.
