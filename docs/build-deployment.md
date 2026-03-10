# Shopizer 2.x Legacy Build and Deployment Profile

## Overview
Shopizer 2.x (legacy fork) is built with Maven and deployed as a WAR (`sm-shop`) into a servlet container. The WAR depends on a shared core library (`sm-core`) which provides persistence, services, and integration modules.

This document describes the build and deployment profile evidenced from Maven POMs and web/Spring configuration.

## Build Toolchain

### Maven modules
- `sm-core`: JAR (`com.shopizer:sm-core:2.0.1-SNAPSHOT`)
- `sm-shop`: WAR (`com.salesmanager:sm-shop:2.0.1-SNAPSHOT`), depends on `sm-core`

Evidence:
- `sm-core/pom.xml`
- `sm-shop/pom.xml`

### Java level
Both modules declare Java 1.6 source/target compatibility:
- `sm-core` uses `<jdk.version>1.6</jdk.version>`
- `sm-shop` uses `<java-version>1.6</java-version>`

Evidence:
- `sm-core/pom.xml`
- `sm-shop/pom.xml`

### Key dependencies (high-level)
`sm-core` includes:
- Spring 3.1
- Hibernate 4.1 + JPA EntityManager
- Ehcache integration (`hibernate-ehcache`) and Ehcache provider usage
- Infinispan (core/tree and cache stores)
- JDBC drivers: MySQL and H2
- JavaMail (`javax.mail`)
- PayPal merchant SDK
- JSON tooling (Jackson 1.x, json-simple)

`sm-shop` includes:
- Spring MVC 3.1
- Spring Security 3.1
- Tiles 2.1.2
- Servlet/JSP APIs (provided)
- reCAPTCHA4J

Evidence:
- `sm-core/pom.xml`
- `sm-shop/pom.xml`

## Packaging and Runtime Shape

### WAR layout and servlet wiring
`sm-shop` is a WAR with:
- `WEB-INF/web.xml` defining:
  - `ContextLoaderListener`
  - `DispatcherServlet` mapped to `/`
  - Spring Security filter chain via `DelegatingFilterProxy`

Spring contexts loaded include:
- `classpath:spring/spring-context.xml` (from `sm-core`)
- `/WEB-INF/spring/root-context.xml`
- `/WEB-INF/spring/appServlet/shopizer-properties.xml`
- `/WEB-INF/spring/appServlet/shopizer-security.xml`
- `/WEB-INF/spring/appServlet/servlet-context.xml`

Evidence:
- `sm-shop/src/main/webapp/WEB-INF/web.xml`

### Spring MVC and view technology
The dispatcher servlet context (`servlet-context.xml`) configures:
- Annotation-driven Spring MVC controllers
- Static resource handler `/resources/**`
- Tiles view resolver and Tiles definitions files
- JSP view resolver
- Message bundles
- Multipart upload support

Evidence:
- `sm-shop/src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml`

### Spring Security
Security configuration (`shopizer-security.xml`) defines:
- Admin authentication (`/admin/**`) with role `AUTH`
- Shop customer authentication (`/shop/customer/**`) with role `AUTH_CUSTOMER`
- Stateless service endpoints (`/services/**`), with `/services/private/**` protected by role `AUTH`

Evidence:
- `sm-shop/src/main/webapp/WEB-INF/spring/appServlet/shopizer-security.xml`

## Persistence and Database Configuration

### JPA/Hibernate wiring
The core Spring context defines an `entityManagerFactory` with:
- Persistence unit `sm-unit`
- Persistence XML at `classpath:META-INF/sm-persistence.xml`
- Database platform from `${hibernate.dialect}`
- Hibernate schema from `${db.schema}`
- Hibernate cache provider set to `org.hibernate.cache.EhCacheProvider`
- Second-level cache enabled

Evidence:
- `sm-core/src/main/resources/spring/spring-context.xml`
- `sm-core/src/main/resources/META-INF/sm-persistence.xml`

### Datasource
The datasource is provided via C3P0 using properties:
- `${db.driverClass}`
- `${db.jdbcUrl}`
- `${db.user}`
- `${db.password}`
- pool sizing and test query properties

Evidence:
- `sm-core/src/main/resources/spring/datasource-c3p0.xml`

A production `database.properties` file was not found in `sm-core/src/main/resources` in this documentation pass. A test database properties file exists under `sm-core/src/test/resources/database.properties` and appears to be base64-encoded content.

## Search Runtime Profile
Search is configured via `shopizer-search.xml` with:
- `ServerConfiguration.mode = local` (embedded)
- `clusterHost` / `clusterPort` available for remote mode
- Multiple workflows for indexing and searching

Evidence:
- `sm-core/src/main/resources/spring/shopizer-search.xml`

Startup search initialization is triggered by `ApplicationContextListenerUtils`.

Evidence:
- `sm-core/src/main/java/com/salesmanager/core/utils/ApplicationContextListenerUtils.java`

## Caching Profile
Two caching technologies are in use:

1. Ehcache for service cache and Hibernate second-level cache:
- Spring cache manager wired in `shopizer-core-ehcache.xml`
- Cache region `com.shopizer.OBJECT_CACHE`
- Cache config in `ehcache/smcore-ehcache.xml`

Evidence:
- `sm-core/src/main/resources/spring/shopizer-core-ehcache.xml`
- `sm-core/src/main/resources/ehcache/smcore-ehcache.xml`

2. Infinispan for CMS/static content cache managers:
- Managers initialize Infinispan caches and expose tree cache structures.

Evidence:
- `sm-core/src/main/java/com/salesmanager/core/modules/cms/impl/CacheManagerImpl.java`

## Deployment Checklist (Practical)
A minimal runtime environment requires:
- A servlet container capable of running a Servlet 2.5-era WAR.
- A JDBC-accessible relational database, with appropriate `database.properties` provided at runtime (classpath or externalized depending on deployment packaging).
- Network access to external gateways used by enabled modules (PayPal, SMTP server, shipping carriers).
- Sufficient filesystem permissions if search is configured in embedded “local” mode, because indices are created in the working directory.

## Follow-ups
For a more operationally complete deployment guide, inspect:
- `sm-core/src/main/resources/spring/shopizer-core-config.xml` and `shopizer-core-modules.xml` to enumerate all module beans and configuration keys.
- Any environment-specific property files or deployment scripts that may exist outside the paths reviewed here.
