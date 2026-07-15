Feature: Find Transactions — verify last account transaction

  Confirms the most recent transaction on a customer's first account
  can be located again through the Find Transactions search.

  Background:
    Given I open the ParaBank home page
    And I log in as "john" with password "demo"

  @regression
  Scenario: Last transaction on the first account is found via Find Transactions
    When I open the activity page for my first account
    And I open the last transaction on that account
    Then I capture the transaction ID and date from the transaction details page
    When I search Find Transactions using the captured transaction ID
    Then the results should display the captured transaction ID