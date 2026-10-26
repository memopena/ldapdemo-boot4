# ldapdemo-boot4

![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.2-6DB33F)
![Spring Security](https://img.shields.io/badge/Spring%20Security-7-6DB33F)

Reference implementation of LDAP authentication and role based authorization with Spring Security 7 and Spring Boot 4. The application runs an embedded LDAP directory, authenticates users with a bind operation and maps LDAP groups to application roles.

It is a modernization of [ldapdemo](https://github.com/memopena/ldapdemo), originally built on Spring Boot 1.5 and Java 8.

## Features

- Embedded LDAP server (UnboundID) seeded from an LDIF file
- Bind authentication with group based roles (`USER`, `ADMIN`)
- `SecurityFilterChain` configuration with the lambda DSL and CSRF protection
- Server side UI with Thymeleaf 3
- User management backed by Spring Data JPA and HSQLDB
- Integration tests for authentication and access rules

## Getting started

Requirements: JDK 25. Maven is provided through the wrapper.

```bash
./mvnw spring-boot:run
```

Open http://localhost:8081 and sign in with one of the demo accounts:

| User  | Password | Role  | Access                                  |
|-------|----------|-------|-----------------------------------------|
| user1 | memo     | USER  | User list and create form               |
| user2 | memo     | ADMIN | Full access, including view and delete  |

Run the tests with:

```bash
./mvnw test
```

## Configuration

| File                                 | Purpose                                     |
|--------------------------------------|---------------------------------------------|
| `DemoSecurityConfig.java`            | Access rules, login flow and LDAP wiring    |
| `src/main/resources/users.ldif`      | Directory content: users and role groups    |
| `src/main/resources/application.properties` | Server port and persistence settings |

To use an external directory, replace the embedded context source in `DemoSecurityConfig` with a `DefaultSpringSecurityContextSource` pointing to your LDAP URL.

## Upgrade notes

Main changes from the original project: Jakarta EE namespaces, Spring Security 7 configuration model, UnboundID in place of ApacheDS, constructor injection, CSRF enabled with POST based logout and delete, and removal of unused dependencies.
