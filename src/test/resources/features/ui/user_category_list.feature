@user @category
Feature: User Category List Management
  As a User
  I want to view and search categories
  But I cannot add, edit, or delete categories

  Background:
    Given User is logged in and on categories page

  @TC_UI_USER_12
  Scenario: Categories Page Load with List for User
    When User navigates to /ui/categories
    Then Categories page should load successfully
    And Categories list should be displayed if data exists

  @TC_UI_USER_13
  Scenario: User View of Empty Categories List
    Given Database has no categories
    When User navigates to /ui/categories
    Then "No category found" message should be displayed

  @TC_UI_USER_14
  Scenario: User Cannot See Add Category Button
    When User navigates to /ui/categories
    Then "Add A Category" button should not be visible

  @TC_UI_USER_15
  Scenario: User Cannot See Edit Action
    Given Multiple categories exist in database
    When User navigates to /ui/categories
    Then Edit buttons should not be visible for any category

  @TC_UI_USER_16
  Scenario: User Cannot See Delete Action
    Given Multiple categories exist in database
    When User navigates to /ui/categories
    Then Delete buttons should not be visible for any category

  @TC_UI_USER_17
  Scenario: User Search Categories Functionality
    Given Multiple categories exist in database
    When User enters search keyword "cat"
    And User clicks Search button
    Then Only matching categories should be displayed

  @TC_UI_USER_18
  Scenario: User Filter by Parent Functionality
    Given Multiple categories exist in database
    And Parent category "Cat Test01" exists
    When User selects parent category "Cat Test01"
    And User clicks Search button
    Then Filtered categories should be displayed

  @TC_UI_USER_19
  Scenario: User Sorting by ID
    Given Multiple categories exist in database
    When User clicks ID column header
    Then Table should be sorted by ID in "any" order

  @TC_UI_USER_20
  Scenario: User Sorting by Name
    Given Multiple categories exist in database
    When User clicks Name column header
    Then Table should be sorted by Name in "any" order

  @TC_UI_USER_21
  Scenario: User Sorting by Parent
    Given Multiple categories exist in database
    When User clicks Parent column header
    Then Table should be sorted by Parent in "any" order

  @TC_UI_USER_22
  Scenario: User Pagination View
    Given More than 10 categories exist
    When User clicks page 2
    Then Categories should be paginated correctly

  @TC_UI_USER_23
  Scenario: User Reset Search Functionality
    Given Multiple categories exist in database
    When User enters search keyword "Test"
    And User clicks Search button
    And User clicks Reset button
    Then Search keyword should be cleared
