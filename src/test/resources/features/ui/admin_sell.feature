@admin @AdminSell 
Feature: Admin Sell Plant
  As an admin
  I want to sell plants
  So that the system correctly updates stock and validates quantities

  @TC_UI_ADMIN_68
  Scenario: Verify validation when quantity exceeds stock
    Given admin is on the sales page
    When admin attempts to sell 15 of "Lemon" (Stock: 10)
    Then an error message should be displayed

  @TC_UI_ADMIN_69
  Scenario: Verify sale with full stock
    Given admin is on the sales page
    When admin sells 10 of "Lemon" (Stock: 10)
    Then sale should be successful
    And stock of "Lemon" should be 0

  @TC_UI_ADMIN_70
  Scenario: Verify sale with valid quantity
    Given admin is on the sales page
    When admin sells 5 of "Rose" (Stock: 20)
    Then sale should be successful
    And stock of "Rose" should be reduced by 5

  @TC_UI_ADMIN_71
  Scenario: Verify stock reduction after sale
    Given admin is on the sales page
    When admin sells 5 of "Rose" (Stock: 20)
    Then stock of "Rose" should be reduced by 5

  @TC_UI_ADMIN_72
  Scenario: Verify cancel button behavior
    Given admin is on the sell plant page
    When admin clicks Cancel button
    Then admin should be redirected to sales page
    And no sale should be created

  @TC_UI_ADMIN_73
  Scenario: Verify error shown without navigation
    Given admin is on the sell plant page
    When admin enters invalid quantity and saves
    Then error should be displayed on the same page

  @TC_UI_ADMIN_74
  Scenario: Verify non-admin access restriction
    Given a non-admin user is logged in
    When user attempts to access sell page directly
    Then user should be redirected to 403 forbidden page

  @TC_UI_ADMIN_75
  Scenario: Verify decimal quantity handling
    Given admin is on the sell plant page
    When admin enters decimal quantity 5.5
    And admin saves the sale
    Then an error should be displayed or quantity should be handled

  @TC_UI_ADMIN_76
  Scenario: Verify sale when stock is 1
    Given admin is on the sales page
    When admin sells 1 of "LemonTea" (Stock: 1)
    Then sale should be successful
    And stock of "LemonTea" should be 0

  @TC_UI_ADMIN_77
  Scenario: Verify confirmation dialog
    Given admin is on the sales page
    And a sale exists in the system
    When admin clicks delete button for a sale
    Then a confirmation dialog should be displayed

  @TC_UI_ADMIN_78
  Scenario: Verify sale deletion
    Given admin is on the sales page
    And at least one sale exists in the system
    When admin deletes a sale and confirms the action
    Then the deleted sale should no longer be displayed in the sales list

  @TC_UI_ADMIN_79
  Scenario: Verify delete cancellation
    Given admin is on the sales page
    And a sale exists in the system
    When admin cancels the deletion
    Then the sale should remain in the list

  @TC_UI_ADMIN_80
  Scenario: Verify stock restored
    Given admin is on the sales page
    And a sale of 5 units exists
    When admin deletes the sale
    Then the plant stock should be increased by 5

  @TC_UI_ADMIN_81
  Scenario: Verify non-admin cannot delete
    Given a non-admin user is logged in
    And the user is on the sales page
    Then delete button should not be visible

  @TC_UI_ADMIN_82
  Scenario: Verify error handling for invalid sale delete
    Given admin is on the sales page
    When admin attempts to delete a non-existent sale with ID 999999
    Then an error should be displayed
