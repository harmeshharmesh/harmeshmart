# Changelog

All notable changes to the **HarmeshMart** multi-vendor marketplace platform will be documented in this file.
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1.0] - 2026-10-09 (Final Review Build)
- feat(ai): integrated server-side AI Chatbot (`/api/v1/chat`, `ChatProvider`, `MockChatProvider`, `GeminiChatProvider`) with rate limiting and per-session response caching.
- feat(ui): added floating AI assistant widget with quick domain FAQ pills and interactive chat drawer.
- docs: finalized project specification compliance, Mermaid architecture diagrams, and review documentation.

## [1.0.0] - 2026-09-21 (Full Build & Cloud Deployment)
- feat(auth): implemented Role-Based Access Control (BUYER, SELLER, ADMIN) with BCrypt password hashing and session fixation regeneration.
- feat(catalog): created multi-category Indian e-commerce catalog with high-res Unsplash photography and ₹ INR currency formatting.
- feat(cart): added dynamic shopping cart with live running subtotal, item removal, and promo code `SAVE10` discount engine.
- feat(checkout): implemented multi-step order checkout with address book and mock payment strategy (UPI QR, Credit/Debit Card, COD).
- feat(orders): added buyer order history tracking and printable GST tax invoice generator.
- feat(seller): built Seller Central Merchant Hub with listing CRUD, stock toggles, and incoming order fulfillment workflow.
- feat(admin): developed Admin Console for seller approvals, catalog moderation, user directory, and platform revenue metrics.
- feat(observability): configured SLF4J MDC request ID logging and `/api/v1/health` DB check.
- test: implemented JUnit 5 DAO tests against embedded H2 and Mockito service validation suites.

## [0.1.0] - 2026-08-10 (MVP Review Build)
- feat(core): initialized Maven project skeleton, layered MVC architecture, and Tomcat 9.0.x deployment configuration.
- feat(db): created relational H2 schema (`V1__init_schema.sql`) and rich Indian catalog seed dataset (`seed.sql`).
