Feature: ParaBank Home Page

  Smoke tests to verify the ParaBank home page is reachable
  and core elements load before other tests run.

  Background:
    Given I open the ParaBank home page

  @smoke
  Scenario: Home page loads with the correct title
    Then the page title should contain "ParaBank"

  @smoke
  Scenario: Login form is present on the home page
    Then the username field should be displayed