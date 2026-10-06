# SDET Portfolio Framework

A hybrid BDD test automation framework demonstrating end-to-end tests across UI, API, and database (JDBC technique demo against a local fixture) layers covering happy-path, negative-case, and data-integrity scenarios, built and run against a real public banking application.

## Application Under Test

ParaBank https://parabank.parasoft.com - Parasoft's public online banking demo. Chosen for its realistic UI workflows and exposed REST API, enabling true end-to-end testing across all three layers without needing a private test environment.

## Project Structure

- src/test/
    - java/com/aliparmar/sdet/
        - pages/ (Page Object Model Classes)
        - stepdefs/ (Cucumber step definitions)
        - runners/ (TestNG Cucumber runners: full BDD run and a @smoke-only run)
        - ui/ (TestNG UI test classes)
        - api/ (REST Assured test classes)
            - client/ (Shared REST Assured client for the money endpoints)
        - db/ (JDBC database test classes)
        - utils/ (Driver factory, config reader, ExtentReports listener, Cucumber scenario context)
        - base/ (Base test classes)
    - resources/
        - features/ (Cucumber .feature files)
        - testng-suites/ (TestNG XML suites: smoke / sanity / regression / bdd / ci)
        - config/ (Environment properties)
        - schemas/ (JSON schemas for contract validation)

## Money-Flow Test Coverage

These tests follow the money through ParaBank and chain each response into the next request.

| Test | Layers | Suite | Chain |
|------|--------|-------|-------|
| New customer registers, opens a savings account and transfers funds | UI (Cucumber) + API | smoke, regression | register through the UI, then login > customerId > accounts > createAccount > transfer > balance checks |
| Deposit then withdraw | API (REST Assured, Hamcrest) | regression | login > accounts > deposit > withdraw > balance and transaction history |
| Bill pay | API (REST Assured, Hamcrest) | regression | login > accounts > deposit > billpay > balance and transactions-by-amount |

## Running the Tests

The framework uses a "-Dsuite" flag to switch between TestNG suite profiles without touching pom.xml:

- Full regression (all UI, API, DB, and Cucumber BDD tests - default)
    - mvn test
- Smoke suite (quick cross-layer sanity check, includes the UI-to-API money-flow scenario)
    - mvn test -Dsuite=smoke
- Sanity suite
    - mvn test -Dsuite=sanity
- BDD suite (Cucumber feature files only)
    - mvn test -Dsuite=bdd
- CI suite (API + DB only, no browser required)
    - mvn test -Dsuite=ci


After each run the test reports are generated at: target/extent-reports/ExtentReport.html (Cucumber reports are in target/cucumber-reports/)

## CI/CD

A GitHub Actions workflow ".github/workflows/ci.yml" runs the suites against headless Chrome:

- Every push and pull request to "main" runs the "ci" suite (API + database tests, no browser) and the "smoke" suite.
- A nightly schedule runs the full "regression" suite.
- The workflow can also be started by hand from the Actions tab, choosing which suite to run.

Each run starts its own ParaBank from the official "parasoft/parabank" Docker image as a service container, so CI does not depend on the shared public server or on other people's data. The workflow waits for ParaBank to come up, checks the REST API, and generates "config.properties" pointing at that container.

The ExtentReport and Cucumber reports are uploaded as build artifacts for every suite on every run. The "DriverFactory" supports a "headless" flag in "config.properties", which the workflow sets to true.

## Notable Engineering Decisions

- JDBC layer uses SQLite, not a hosted database. This keeps the demo fully self-contained and runnable by anyone who clones the repo — same connection-and-query pattern you'd use against MySQL or Postgres, just a different connection string.
- Suite profiles are switched via a Maven property "-Dsuite", not hardcoded. This lets one "pom.xml" serve smoke, sanity, CI, and full regression runs without duplicated Surefire configuration.
- API tests extract IDs dynamically rather than hardcoding them. ParaBank's public demo dataset is shared and resets periodically, so a hardcoded account ID is fragile. Tests instead query for a customer's accounts first and chain the returned ID into the next request — the same pattern used against real, frequently-changing production data.
- Money is handled as BigDecimal and every balance assertion is relative to a balance read at the start of the test, never a hardcoded figure. This keeps the money-flow tests valid on a shared dataset and avoids floating-point rounding failures.
- One REST Assured client is shared by the Cucumber steps and the TestNG API tests, so the UI-to-API scenario and the pure API tests exercise the endpoints the same way.
- Cucumber steps share data (the newly registered credentials) through PicoContainer dependency injection instead of static fields.

## Author

**Ali Parmar**
- SDET / Test Automation Engineer
- Houston, TX
- LinkedIn: https://www.linkedin.com/in/aliparmar
- Email:ali.parmar@att.net