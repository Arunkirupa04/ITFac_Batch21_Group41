@admin
Feature: Login & Dashboard Functionality
  As an admin
  I want to authenticate and access the dashboard
  So that I can manage the system securely

  @TC_UI_ADMIN_01
  Scenario: Successful Admin Login and Dashboard Redirection
    Given user is on login page
    When user enters username "admin" and password "admin123"
    And user clicks login button
    Then user should be redirected to dashboard
    And close the browser

  @TC_UI_ADMIN_02
  Scenario: Invalid Username Error Display
    Given user is on login page
    When user enters username "invaliduser" and password "admin123"
    And user clicks login button
    Then error message "Invalid username or password" should be displayed

  @TC_UI_ADMIN_03
  Scenario: Invalid Password Error Display
    Given user is on login page
    When user enters username "admin" and password "wrongpass"
    And user clicks login button
    Then error message "Invalid username or password" should be displayed

  @TC_UI_ADMIN_04
  Scenario: Empty Username Field Validation
    Given user is on login page
    When user enters username "" and password "admin123"
    And user clicks login button
    Then field error "Username is required" should be displayed

  @TC_UI_ADMIN_05
  Scenario: Empty Password Field Validation
    Given user is on login page
    When user enters username "admin" and password ""
    And user clicks login button
    Then field error "Password is required" should be displayed

  @TC_UI_ADMIN_06
  Scenario: Both Username and Password Empty Validation
    Given user is on login page
    When user enters username "" and password ""
    And user clicks login button
    Then field error "Username is required" should be displayed
    And field error "Password is required" should be displayed

  @TC_UI_ADMIN_07
  Scenario: Logout Success Message Display
    Given user is logged in as admin
    When user clicks logout
    Then user should see message "You have been logged out successfully"

  @TC_UI_ADMIN_08
  Scenario: Protected Page Access Without Login
    Given user is not logged in
    When user navigates to dashboard page
    Then user should be redirected to login page

  @TC_UI_ADMIN_09
  Scenario: Dashboard Load After Admin Login
    Given user is logged in as admin
    Then dashboard should be displayed with summary cards

  @TC_UI_ADMIN_10
  Scenario: Initial Summary Cards Values
    Given user is logged in as admin
    Then dashboard summary cards should show initial values

  @TC_UI_ADMIN_11
  Scenario: Navigation Button Functionality for Admin
    Given user is logged in as admin
    When admin clicks Manage Categories button
    Then admin should be navigated to categories page

  @TC_UI_ADMIN_12
  Scenario: Active Dashboard Page Highlight
    Given user is logged in as admin
    Then dashboard menu item should be highlighted
