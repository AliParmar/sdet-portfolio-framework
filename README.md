# SDET Portfolio Framework

A hybrid BDD test automation framework built to demonstrate end-to-end SDET capabilities across UI, API, and database layers.

> 🚧 **Work in progress** — actively being built. Sections marked with checkboxes are complete; others are in development.

## Tech Stack

- **Language:** Java 21
- **Build:** Maven
- **UI Automation:** Selenium WebDriver 4
- **Test Runner:** TestNG
- **BDD:** Cucumber 7
- **API Testing:** REST Assured + JSON Schema Validator
- **Database Testing:** JDBC with SQLite
- **Reporting:** Cucumber HTML reports + ExtentReports
- **CI/CD:** GitHub Actions + Jenkins
- **Test Management:** Jira + Xray (integration-ready)

## Application Under Test

[ParaBank](https://parabank.parasoft.com) — Parasoft's public online banking demo, chosen for its realistic UI workflows and exposed REST API, enabling true end-to-end testing across all three layers.

## Project Structure

    src/test/
    ├── java/com/aliparmar/sdet/
    │   ├── pages/        Page Object Model classes
    │   ├── stepdefs/     Cucumber step definitions
    │   ├── runners/      TestNG Cucumber runners
    │   ├── api/          REST Assured test classes
    │   ├── db/           JDBC database test classes
    │   ├── utils/        Helpers (driver factory, config readers)
    │   └── base/         Base test classes
    └── resources/
        ├── features/      Cucumber .feature files
        ├── testng-suites/ TestNG XML suites (smoke, sanity, regression)
        ├── config/        Environment properties
        └── schemas/       JSON schemas for contract validation

## Progress

- [x] Maven project scaffold with full SDET dependency stack
- [x] Folder structure for hybrid framework
- [x] Initial Git repository and GitHub integration
- [x] First Selenium + TestNG smoke test
- [x] Page Object Model layer
- [ ] Cucumber BDD layer with feature files
- [ ] REST Assured API tests
- [ ] JDBC database testing layer
- [ ] HTML reporting (ExtentReports)
- [ ] Jira + Xray integration
- [ ] CI/CD pipeline (GitHub Actions + Jenkinsfile)
- [ ] Test suite profiles (smoke / sanity / regression)
- [ ] Polished documentation with screenshots

## Author

**Ali Parmar** — SDET / Test Automation Engineer
Houston, TX
[LinkedIn](https://www.linkedin.com/in/aliparmar) • [Email](mailto:ali.parmar@att.net)