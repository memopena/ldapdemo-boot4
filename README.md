# ldapdemo-boot4

LDAP based user authentication with Spring Security and Spring Boot, updated to current versions.
This is the modernized version of [ldapdemo](https://github.com/memopena/ldapdemo) (Spring Boot 1.5, Java 8).

## Stack

- Java 25 (LTS)
- Spring Boot 4.1.2 / Spring Security 7
- Embedded LDAP server (UnboundID) loaded from `src/main/resources/users.ldif`
- Thymeleaf 3 with Layout Dialect
- Spring Data JPA with in-memory HSQLDB

## Run

```bash
./mvnw spring-boot:run
```

Open http://localhost:8081 and sign in with one of the LDAP users:

| User  | Password | Role  | Access                         |
|-------|----------|-------|--------------------------------|
| user1 | memo     | USER  | User list and create form      |
| user2 | memo     | ADMIN | Everything under `/user/**`    |

## Test

```bash
./mvnw test
```

The tests authenticate against the embedded LDAP server and check the role based access rules.

## What changed from the original

- Spring Boot 1.5.3 to 4.1.2, Java 8 to 25.
- `javax.*` to `jakarta.*` (persistence, validation).
- `WebSecurityConfigurerAdapter` replaced by a `SecurityFilterChain` bean using the lambda DSL.
- Embedded ApacheDS (removed from Spring Security) replaced by UnboundID via `EmbeddedLdapServerContextSourceFactoryBean` and `LdapBindAuthenticationManagerFactory`.
- CSRF protection enabled. Logout and delete are now POST forms.
- Constructor injection instead of field injection.
- `@EnableWebMvc` removed so Spring Boot's MVC auto-configuration applies.
- Unused dependencies removed (JSP, JSTL, Guava, Commons Lang, springloaded, old jQuery).
- The JPA table is now `app_user`, since `user` is a reserved word.
