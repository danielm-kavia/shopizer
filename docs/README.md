# Shopizer 2.x (Legacy Fork) Living Documentation

## Overview
This folder contains “living” documentation regenerated from the current state of the legacy Shopizer 2.x fork found under `shopizer/`. The goal is to provide a code-backed map of how the application is structured, how it runs, how data is persisted, and how it integrates with external systems.

The repository is a classic Java/Spring monolith split into a core library module and a web application module.

## Documentation Map
Read these documents in the following order.

1. [Architecture Map](./architecture-map.md) describes the system context, runtime container, internal components, and key end-to-end flows.
2. [Module and Package Map](./modules.md) explains the Maven modules and the major Java package areas and responsibilities.
3. [Data Model](./data-model.md) documents the primary persistence model, key entities, and relationships as evidenced from JPA mappings and `sm-persistence.xml`.
4. [Service Layer](./service-layer.md) describes the service layer patterns, key services, and how cross-cutting concerns such as caching, encryption-backed configuration, and search initialization are wired.
5. [External Integrations](./external-integrations.md) summarizes payment, shipping, email, search, caching, and other integration points, including where configuration is stored.
6. [Build and Deployment Profile](./build-deployment.md) documents the build toolchain, runtime assumptions, and packaging/deployment shape (WAR deployment with Spring MVC and Spring Security).

## Scope and Evidence
These documents are based on concrete repository artifacts, primarily:
- Maven POMs for dependency and build configuration.
- Web deployment descriptors (`web.xml`) and Spring XML contexts under `WEB-INF`.
- Spring core wiring in `sm-core` (`spring-context.xml` and imported context files).
- JPA persistence unit declaration (`META-INF/sm-persistence.xml`) and representative domain entities.
- Representative service implementations for payments, transactions, email, caching, and search initialization.

Where information is not available from current sources, the relevant document explicitly calls that out and suggests what to inspect next.
