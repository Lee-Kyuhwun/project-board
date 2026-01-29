# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## ⚠️ CRITICAL: Documentation Update Policy

**YOU MUST UPDATE THIS FILE AFTER EVERY SIGNIFICANT CHANGE**

After completing ANY of the following tasks, you MUST update both `CLAUDE.md` and `AGENTS.md`:
- Implementing new features (controllers, services, views)
- Modifying architecture or design patterns
- Adding/removing dependencies
- Changing build configurations
- Discovering new issues or solutions
- Completing major bug fixes

**Update Process**:
1. Complete your implementation task
2. Review and update the "Current Implementation Status" section below
3. Add new patterns/issues to relevant sections (Common Issues, Key Architectural Decisions, etc.)
4. Update AGENTS.md with the same information
5. Commit the documentation changes along with your code changes

**This ensures the next session has accurate, up-to-date context.**

## Current Implementation Status

### ✅ Implemented Features
- **Header Navigation** (header.html, header.th.xml):
  - Home link to article list (`/articles`)
  - Hashtags link to hashtag search page (`/articles/search-hashtags`)
  - Thymeleaf decoupled logic for safe static file display
- **Footer Navigation** (footer.html, footer.th.xml):
  - Home link with Thymeleaf template tag
- **Article List View** (`GET /articles`): Pagination, sorting, search functionality
- **Article Search** (`GET /articles?searchType=...&searchValue=...`): By TITLE, CONTENT, ID, NICKNAME, HASHTAG
- **Hashtag Search Page** (`GET /articles/search-hashtags?searchValue=...`): Dedicated hashtag search with hashtag list accessible from header
- **Article Detail View** (`GET /articles/{id}`): Full implementation with dynamic data binding (ArticleController.java:46-54)
  - Thymeleaf template (`detail.html`) with activated `detail.th.xml`
  - Displays article content, comments, author info, hashtags
  - Navigation to prev/next articles
- **Article Create**:
  - Form view (`GET /articles/form`): ArticleController.java:76-80
  - Create endpoint (`POST /articles/form`): ArticleController.java:82-88
  - Shared form template (`form.html` and `form.th.xml`) for create/update
  - "글쓰기" button in article list page
- **Article Update**:
  - Form view (`GET /articles/{id}/form`): ArticleController.java:90-96
  - Update endpoint (`POST /articles/{id}/form`): ArticleController.java:98-104
  - Pre-populated form with existing article data
- **Article Delete**:
  - Delete endpoint (`POST /articles/{id}/delete`): ArticleController.java:106-111
  - Delete button in article detail page

### ⚠️ Partially Implemented
- **Authentication & Authorization**:
  - Hardcoded user "uno" for all operations (ArticleController.java:85, 101)
  - TODO comments indicate future Spring Security integration
  - No user validation for update/delete operations
- **Comment Display**:
  - Comments rendered in detail page (simplified version)
  - Child comments and comment actions removed (not supported by current Response DTOs)

### ❌ Not Implemented
- **Comment CRUD**:
  - Comment create endpoint (`POST /comments/new`)
  - Comment delete endpoint (`POST /comments/{id}/delete`)
  - Comment form exists in detail.html but no backend support
- **User Authentication**:
  - No login/logout functionality
  - Spring Security configured to permitAll
  - JpaConfig uses hardcoded auditor "uno"

### 🔧 Next Steps
1. Implement Spring Security with actual user authentication
2. Add user validation for article update/delete (owner check)
3. Implement comment CRUD functionality
4. Add ArticleRequest/ArticleResponse validation
5. Update JpaConfig to use authenticated user for auditing

## Build & Development Commands

This project uses **Gradle 7.6.1** with the Spring Boot plugin.

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Run all tests
./gradlew test

# Run a single test class
./gradlew test --tests ArticleControllerTest

# Run a specific test method
./gradlew test --tests ArticleControllerTest.givenNothing_whenRequestingArticlesView_thenReturnsArticlesView

# Clean build (important: deletes QueryDSL generated classes)
./gradlew clean build

# Compile only (regenerates QueryDSL Q-classes)
./gradlew compileJava
```

**Important**: After modifying entity classes, run `./gradlew clean compileJava` to regenerate QueryDSL Q-classes in `src/main/generated/`.

## High-Level Architecture

### Layered Architecture
```
Controller Layer (Thymeleaf views)
    ↓
Service Layer (business logic)
    ↓
Repository Layer (JPA + QueryDSL)
    ↓
Domain/Entity Layer
    ↓
Database (MySQL/H2)
```

### Technology Stack
- **Spring Boot 2.7.15** with Java 17
- **Spring Data JPA** with Hibernate ORM
- **QueryDSL 5.0.0** for type-safe queries
- **Thymeleaf 3** with decoupled template logic (`.th.xml` files) for server-side rendering
  - HTML templates (`.html`) contain static mockup content
  - Thymeleaf logic files (`.th.xml`) contain dynamic data binding
  - This separation allows HTML files to be viewed standalone without Spring Boot
- **Spring Security** (currently permits all with form login)
- **MySQL** (dev) / **H2** (tests)
- **Spring Data REST** with HAL Explorer at `/api`

### Key Architectural Decisions

**DTO Conversion Pattern**: Multi-layer conversion for separation of concerns
- Form submissions: `Request` → `Dto` → `Entity`
- Data retrieval: `Entity` → `Dto` → `Response`
- Service layer works with `Dto` objects only
- Controller layer converts `Request` to `Dto` and `Dto` to `Response`
- Example: `ArticleRequest.toDto()` → `ArticleService.saveArticle(dto)` → `ArticleDto.toEntity()`

**Search Functionality**: Enum-based search types
- `SearchType` enum: TITLE, CONTENT, ID, NICKNAME, HASHTAG
- Service layer uses switch expression for different query methods
- Hashtag searches automatically prepend "#" if missing

**Pagination**: Custom service calculates visible page numbers
- `PaginationService` generates centered bar of 5 page numbers
- Returns `List<Integer>` of page numbers to display

## QueryDSL Custom Repository Pattern

**Critical Pattern**: When implementing custom QueryDSL repositories that extend `QuerydslRepositorySupport`:

```java
public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport
    implements ArticleRepositoryCustom {

    // MUST use no-arg constructor passing entity class to super
    public ArticleRepositoryCustomImpl() {
        super(Article.class);
    }

    @Override
    public List<String> findAllDistinctHashtags() {
        QArticle article = QArticle.article;
        return from(article)
            .distinct()
            .select(article.hashtag)
            .where(article.hashtag.isNotNull())
            .fetch();
    }
}
```

**Why this matters**: Spring cannot autowire `Class<?>` parameters. The no-arg constructor must explicitly pass the entity class to the superclass.

**Generated Q-Classes Location**: `src/main/generated/` (excluded from version control, regenerated on compile)

## Database Configuration

### Development Environment
```yaml
spring.datasource:
  url: jdbc:mysql://localhost:3307/board
  username: root
  password: 1111
```

- **DDL Auto**: `create` (recreates tables on startup)
- **Show SQL**: Enabled with formatting
- **Open-in-view**: Disabled (explicit transaction boundaries)

### Test Environment
Activate with profile `testdb`:
```bash
./gradlew test -Pprofile=testdb
```

Uses H2 in-memory database with MySQL compatibility mode:
```yaml
spring.datasource:
  url: jdbc:h2:mem:board;mode=mysql
```

Test data automatically loaded from `src/test/resources/data.sql` (123 articles with comments).

## JPA Auditing

JPA Auditing is enabled with a **hardcoded auditor** in `JpaConfig`:

```java
@Bean
public AuditorAware<String> auditorAware(){
    return () -> Optional.of("uno"); // TODO: integrate with Spring Security
}
```

This automatically populates:
- `createdAt`, `modifiedAt` (timestamps)
- `createdBy`, `modifiedBy` (currently always "uno")

**Note**: When Spring Security authentication is implemented, update this bean to return the authenticated user.

## Testing Strategy

### Test Types

**Controller Tests** (`@WebMvcTest`):
- Mock MVC testing with mocked services
- Import `SecurityConfig` for security configuration
- Example: `ArticleControllerTest`

**Service Tests** (Mockito):
- Unit tests with `@ExtendWith(MockitoExtension.class)`
- Mock repositories with BDD-style given/when/then
- Example: `ArticleServiceTest`

**Repository Tests** (`@DataJpaTest`):
- Integration tests with real H2 database
- Must import `JpaConfig` for auditing support
- Uses test data from `data.sql`
- Example: `JpaRepositoryTest`

### Running Tests

```bash
# All tests
./gradlew test

# Specific test class
./gradlew test --tests ArticleControllerTest

# Tests with specific profile
./gradlew test -Pprofile=testdb
```

## Entity Relationships

**Article** (main entity):
- `ManyToOne` with `UserAccount` (author)
- `OneToMany` with `ArticleComment` (cascade ALL, orphan removal)

**ArticleComment**:
- `ManyToOne` with `Article`
- `ManyToOne` with `UserAccount` (commenter)

**AuditingFields** (base class):
- `@MappedSuperclass` providing createdAt, createdBy, modifiedAt, modifiedBy
- Automatically populated by JPA Auditing

## Thymeleaf Decoupled Template Logic

This project uses Thymeleaf's decoupled template logic pattern for clean separation of concerns:

**File Structure**:
- `*.html`: Static HTML mockup with placeholder content
- `*.th.xml`: Thymeleaf logic that binds data to the HTML template

**Benefits**:
- HTML files can be opened directly in browsers without a server
- Designers can work on HTML files without touching Thymeleaf syntax
- Logic is centralized in `.th.xml` files for easier maintenance

**Example**:
```xml
<!-- header.th.xml -->
<thlogic>
    <attr sel="#home" th:href="@{/articles}">Home</attr>
    <attr sel="#hashtag" th:href="@{/articles/search-hashtags}">Hashtags</attr>
</thlogic>
```

**Important**: Both `.html` and `.th.xml` files must be in the same directory for Thymeleaf to apply the logic.

## Common Issues

### QueryDSL Q-classes not found
**Solution**: Run `./gradlew clean compileJava` to regenerate classes in `src/main/generated/`

### Application fails to start with "No qualifying bean of type 'java.lang.Class'"
**Cause**: Custom QueryDSL repository has parameterized constructor
**Solution**: Use no-arg constructor calling `super(EntityClass.class)`

### Tests fail with auditing fields null
**Cause**: Missing `@Import(JpaConfig.class)` on `@DataJpaTest`
**Solution**: Add import annotation to enable JPA Auditing in tests

### IntelliJ not recognizing generated Q-classes
**Solution**:
1. Run `./gradlew clean compileJava`
2. In IntelliJ: File → Invalidate Caches → Invalidate and Restart
3. Mark `src/main/generated` as "Generated Sources Root"

### Thymeleaf template not rendering dynamic data
**Cause**: Missing or incorrectly named `.th.xml` file
**Solution**:
1. Ensure `.th.xml` file exists in the same directory as `.html` file
2. File names must match exactly (e.g., `header.html` → `header.th.xml`)
3. Check XML syntax in `.th.xml` file (must start with `<?xml version="1.0"?>` and wrap content in `<thlogic>` tag)
