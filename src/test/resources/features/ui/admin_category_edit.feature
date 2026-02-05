@admin @category @edit
Feature: Admin Edit Category Functionality

  Background:
    Given Admin is logged in and on categories page

  @TC_UI_ADMIN_27
  Scenario: Valid Category Name Edit
    Given Category "Cat Test01" exists
    When Admin clicks Edit for category "Cat Test01"
    And Admin modifies category name to unique name with prefix "Revised"
    And Admin clicks Save button
    Then Category should be updated successfully
    And Close the browser

  @TC_UI_ADMIN_28
  Scenario: Name Clearing Validation During Edit
    Given Category "Cat Test01" exists
    When Admin clicks Edit for category "Cat Test01"
    And Admin clears category name field
    And Admin clicks Save button
    Then Validation error should be shown
    And Close the browser

  @TC_UI_ADMIN_29
  Scenario: Cancel Edit Redirect
    Given Category "Cat Test01" exists
    When Admin clicks Edit for category "Cat Test01"
    And Admin clicks Cancel button
    Then User should be redirected to category list
    And Close the browser
