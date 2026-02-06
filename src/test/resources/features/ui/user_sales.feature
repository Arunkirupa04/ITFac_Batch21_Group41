@UserSales @user
Feature: User Sales and Dashboard
  As a standard user
  I want to view sales, verify restrictions, and check dashboard summaries
  So that I can use the application according to my role

  @TC_UI_USER_51
  Scenario: Verify User Sell Plant button visibility for Admin
    Given a standard user is logged in
    And user is on the sales page
    Then the "Sell Plant" button should not be visible

  @TC_UI_USER_52
  Scenario: Verify User delete button visibility for Admin
    Given a standard user is logged in
    And user is on the sales page
    And sales exist in the system
    Then the delete button for sales should not be visible

#  @TC_UI_USER_53
#  Scenario: Verify User dropdown excludes zero stock plants
#    Given a standard user is logged in
#    And mixed stock plants exist including zero stock
#    When user opens the plant dropdown on the sell page
#    Then zero stock plants should not be visible in the dropdown
#
#  @TC_UI_USER_54
#  Scenario: Verify User validation for empty plant
#    Given a standard user is logged in
#    And user is on the sell plant page
#    When user leaves plant empty and tries to save
#    Then a "Plant is required" error should be displayed
#
#  @TC_UI_USER_55
#  Scenario: Verify User validation for empty quantity
#    Given a standard user is logged in
#    And user is on the sell plant page
#    When user selects a plant but leaves quantity empty and saves
#    Then a quantity error should be displayed
#
#  @TC_UI_USER_56
#  Scenario: Verify User validation for zero quantity
#    Given a standard user is logged in
#    When user enters quantity 0 and saves
#    Then a quantity error should be displayed
#
#  @TC_UI_USER_57
#  Scenario: Verify User validation for negative quantity
#    Given a standard user is logged in
#    When user enters quantity -5 and saves
#    Then a quantity error should be displayed
#
#  @TC_UI_USER_58
#  Scenario: Verify User validation when quantity exceeds stock
#    Given a standard user is logged in
#    When user enters quantity 15 for a plant with stock 10
#    Then an error related to stock should be displayed
#
#  @TC_UI_USER_59
#  Scenario: Verify User sale with full stock
#    Given a standard user is logged in
#    And a plant exists with specific stock
#    When user sells quantity equal to stock
#    Then the sale should be created and stock reduced to 0

  @TC_UI_USER_60
  Scenario: Verify summary data on User dashboard is accurate
    Given a standard user is logged in
    And the database has known counts of Categories, Plants, and Sales
    When user navigates to the Dashboard
    Then the Dashboard should show accurate summary counts matching the database

  @TC_UI_USER_61
  Scenario: Verify User can navigate using sidebar menu
    Given a standard user is logged in
    When user clicks on "Dashboard" in sidebar
    And user clicks on "Categories" in sidebar
    And user clicks on "Plants" in sidebar
    And user clicks on "Sales" in sidebar
    Then user should land on the respective pages

  @TC_UI_USER_62
  Scenario: Verify active page is highlighted in sidebar for User
    Given a standard user is logged in
    When user navigates to "Dashboard"
    Then the "Dashboard" link in sidebar should be highlighted
    When user navigates to "Sales"
    Then the "Sales" link in sidebar should be highlighted
