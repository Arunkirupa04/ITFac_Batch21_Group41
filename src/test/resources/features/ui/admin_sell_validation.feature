@admin @AdminSellValidation
Feature: Admin Sell Plant Validation
  As an admin
  I want to be prevented from entering invalid data
  So that sales records remain accurate

  @TC_UI_ADMIN_61
  Scenario: Verify Sell Plant Button Visibility
    Given admin is on the sales page
    Then the "Sell Plant" button should be visible and enabled

  @TC_UI_ADMIN_62
  Scenario: Verify Delete Action Visibility
    Given admin is on the sales page
    And at least one sale exists in the system
    Then a delete button should be visible for each sale row

  @TC_UI_ADMIN_63
  Scenario: Verify Plant Dropdown Stock Filter
    Given admin is on the sell plant page
    Then the plant dropdown should only contain plants with stock greater than 0

  @TC_UI_ADMIN_64
  Scenario: Verify Empty Plant Validation
    Given admin is on the sell plant page
    When admin attempts to sell without selecting a plant
    Then an error message should be displayed

  @TC_UI_ADMIN_65
  Scenario: Verify Empty Quantity Validation
    Given admin is on the sell plant page
    When admin selects a plant but leaves quantity empty
    And admin saves the sale
    Then an error message should be displayed

#  @TC_UI_ADMIN_66
#  Scenario: Verify Zero Quantity Validation
#    Given admin is on the sell plant page
#    When admin selects a plant
#    And admin enters quantity "0"
#    And admin saves the sale
#    Then an error message should be displayed
#
#  @TC_UI_ADMIN_67
#  Scenario: Verify Negative Quantity Validation
#    Given admin is on the sell plant page
#    When admin selects a plant
#    And admin enters quantity "-1"
#    And admin saves the sale
#    Then an error message should be displayed

  @TC_UI_ADMIN_66 @TC_UI_ADMIN_67
  Scenario Outline: Verify Quantity Validation
    Given admin is on the sell plant page
    When admin selects a plant
    And admin enters quantity "<quantity>"
    And admin saves the sale
    Then an error message should be displayed
    Examples:
      | quantity |
      | 0        |
      | -1       |