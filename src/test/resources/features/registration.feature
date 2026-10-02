Feature: Customer Registration

  End-to-end registration journey: a brand new customer signs up and
  can immediately log in with the credentials they just created.

  @regression
  Scenario: A new customer registers and can log in with those credentials
    Given I open the ParaBank home page
    When I navigate to the registration page
    And I register a new customer with a unique username
    Then the registration should be confirmed
    When I log out
    And I log in with the newly registered credentials
    Then I should land on the Accounts Overview page