@admin @category
Feature: Admin Category List Management

  Background:
    Given Admin is logged in and on categories page

  @TC_UI_ADMIN_13
  Scenario: Categories Page Load for Admin with Empty or Populated List
    When Admin navigates to /ui/categories
    Then Categories page should load successfully
    And Search, Filter, Reset, Add A Category buttons should be visible
    And Close the browser

  @TC_UI_ADMIN_14
  Scenario: Sorting by ID Column
    Given Multiple categories exist in database
    When Admin clicks ID column header
    Then Table should be sorted by ID in "any" order
    And Close the browser

  @TC_UI_ADMIN_15
  Scenario: Search by Sub Category Name Filter
    Given Multiple categories exist in database
    When Admin enters search keyword "cat"
    And Admin clicks Search button
    Then Only matching categories should be displayed
    And Close the browser

  @TC_UI_ADMIN_16
  Scenario: Reset Button Functionality
    Given Multiple categories exist in database
    When Admin enters search keyword "Test"
    And Admin clicks Search button
    And Admin clicks Reset button
    Then Search keyword should be cleared
    And Close the browser

  @TC_UI_ADMIN_17
  Scenario: Parent Category Filter Functionality
    Given Multiple categories exist in database
    And Parent category "Cat Test01" exists
    When Admin selects parent category "Cat Test01"
    And Admin clicks Search button
    Then Filtered categories should be displayed
    And Close the browser

  @TC_UI_ADMIN_18
  Scenario: Categories List Pagination
    Given More than 10 categories exist
    When Admin clicks page 2
    Then Categories should be paginated correctly
    And Close the browser

  @TC_UI_ADMIN_19
  Scenario: Empty Categories List State
    When Admin navigates to /ui/categories
    Then Categories page should load successfully
    And Close the browser

  @TC_UI_ADMIN_20
  Scenario: Admin Add Category Button Visibility
    When Admin navigates to /ui/categories
    Then Add Category button should be visible
    And Close the browser
