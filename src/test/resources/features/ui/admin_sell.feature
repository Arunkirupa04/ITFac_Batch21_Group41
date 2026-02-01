Feature: Admin Sell Plant

  Scenario: Admin sells a plant
    Given admin is on the sales page
    When admin sells 2 of "Rose (Stock: 4)"
    Then sale should be successful
