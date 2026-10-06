Feature: New customer money flow

  A brand new customer registers through the UI, then the REST API proves
  money moves correctly between that customer's accounts.

  @smoke @api
  Scenario: A newly registered customer can open a savings account and transfer funds
    Given I open the ParaBank home page
    When I navigate to the registration page
    And I register a new customer with a unique username
    Then the registration should be confirmed
    When I log in to the API with the newly registered credentials
    Then the customer should have a checking account with a positive balance
    When I open a new savings account funded from the checking account
    And I transfer 50.00 from checking to the new savings account
    Then the checking balance should be reduced by 50.00
    And the savings balance should be increased by 50.00
    And the combined balance of both accounts should be unchanged by the transfer