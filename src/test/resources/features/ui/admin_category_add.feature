@admin @category @add
Feature: Admin Add Category Functionality

  Background:
    Given Admin is logged in and on categories page

  @TC_UI_ADMIN_21
  Scenario: Valid Main Category Addition
    When Admin clicks Add A Category button
    And Admin enters unique category name with prefix "Outdoor"
    And Admin selects category type "Main Category"
    And Admin clicks Save button
    Then Category should be created successfully
    And Close the browser

  @TC_UI_ADMIN_22
  Scenario: Sub-Category Addition with Parent
    Given Parent category "Cat Test01" exists
    When Admin clicks Add A Category button
    And Admin enters unique category name with prefix "Ferns"
    And Admin selects category type "Sub Category"
    And Admin selects parent category "Cat Test01"
    And Admin clicks Save button
    Then Sub-category should be created with parent
    And Close the browser

  @TC_UI_ADMIN_23
  Scenario: Cancel Addition Redirect
    When Admin clicks Add A Category button
    And Admin clicks Cancel button
    Then User should be redirected to category list
    And Close the browser

  @TC_UI_ADMIN_24
  Scenario: Empty Name Field Validation
    When Admin clicks Add A Category button
    And Admin leaves name field empty
    And Admin clicks Save button
    Then Validation message "required" should be displayed
    And Close the browser

  @TC_UI_ADMIN_25
  Scenario: Name Minimum Length Validation
    When Admin clicks Add A Category button
    And Admin enters category name "Ab"
    And Admin clicks Save button
    Then Validation message "length" should be displayed
    And Close the browser

  @TC_UI_ADMIN_26
  Scenario: Name Maximum Length Validation
    When Admin clicks Add A Category button
    And Admin enters category name "FlowerPower1"
    And Admin clicks Save button
    Then Validation message "length" should be displayed
    And Close the browser
