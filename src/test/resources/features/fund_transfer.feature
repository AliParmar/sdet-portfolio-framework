Feature: Fund Transfer

  Verifies that moving money between two of a customer's own accounts
  is reflected correctly on both account balances.

  Background:
    Given I open the ParaBank home page

  @sanity
  Scenario: Transferring funds between two of my accounts updates both balances
    Given I log in with valid credentials
    And I note the balances of my first and second accounts
    When I transfer 50.00 from my first account to my second account
    Then my first account balance should decrease by 50.00
    And my second account balance should increase by 50.00
