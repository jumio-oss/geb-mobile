Feature: JumioLogin
  Scenario: Test good login
    Given show login screen
    When enter login 'rudolf'
    And enter password '12345'
    And click ok
    Then show success screen