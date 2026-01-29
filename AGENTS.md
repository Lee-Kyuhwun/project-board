# Repository Guidelines

## ⚠️ CRITICAL: Documentation Update Policy

**YOU MUST UPDATE THIS FILE AFTER EVERY SIGNIFICANT CHANGE**

After completing ANY of the following tasks, you MUST update both `AGENTS.md` and `CLAUDE.md`:
- Implementing new features (controllers, services, views)
- Modifying architecture or design patterns
- Adding/removing dependencies
- Changing build configurations
- Discovering new issues or solutions
- Completing major bug fixes

**Update Process**:
1. Complete your implementation task
2. Review and update the "Current Implementation Status" section in CLAUDE.md
3. Add new patterns/issues to relevant sections (Common Issues, Key Architectural Decisions, etc.)
4. Update this AGENTS.md file with the same information
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

## Project Structure & Module Organization
- `src/main/java/com/fastcampus/projectboard`: domain entities, DTOs, services, controllers, config, and repositories.
- `src/main/resources/templates`: Thymeleaf views (see `articles/`), with static assets in `src/main/resources/static/`.
- QueryDSL generates Q-types to `src/main/generated`; `gradlew clean` removes stale output.
- Tests live under `src/test/java/com/fastcampus/projectboard` by layer; ERD/use-case diagrams sit in `document/`.

## Build, Test, and Development Commands
- `./gradlew bootRun`: start the app using the default MySQL config at `localhost:3307/board`.
- `SPRING_PROFILES_ACTIVE=testdb ./gradlew test`: run the JUnit 5 suite against the in-memory H2 profile.
- `./gradlew clean build`: regenerate QueryDSL sources and build the bootable jar in `build/libs/`.
- `./gradlew clean`: remove build artifacts and `src/main/generated` to avoid stale Q-classes.

## Coding Style & Naming Conventions
- Java 17, Spring Boot 2.7; enable annotation processing for Lombok/QueryDSL in your IDE.
- 4-space indentation, avoid overly long lines (~120 chars); prefer constructor injection and package-private visibility where sensible.
- Classes use PascalCase; methods/fields camelCase; request mappings noun-based (`/api/articles`); template files kebab-case.
- DTOs end with `Dto`; QueryDSL types start with `Q`; repository interfaces follow Spring Data naming for derived queries.

## Testing Guidelines
- Uses `spring-boot-starter-test` (JUnit 5). Default tests rely on `testdb` profile and H2; seed data per test when needed.
- Name test classes `<Type>Test`/`<Type>Tests`; use descriptive method names such as `findArticlesByTitle_containsKeyword`.
- Cover repository queries (including QueryDSL predicates) and controller/service flows; assert HTTP status, view names, and payload shape.
- Keep tests isolated—avoid cross-test data coupling; prefer factory/builders over reusing mutable fixtures.

## Commit & Pull Request Guidelines
- Mirror existing history: `#<issue>- <summary>` (imperative mood; add short scope in Korean/English as needed).
- Before opening a PR: summarize changes, link issues, attach UI screenshots if templates/static assets changed, and note DB profile used.
- Ensure `./gradlew test` passes; call out any migrations, generated sources, or manual steps for reviewers.

## Security & Configuration Tips
- Never commit credentials; override `spring.datasource.*` via env vars or `application-*.yaml` ignored locally.
- Use `testdb` for CI/local tests; keep optional OAuth settings commented until provided securely.
