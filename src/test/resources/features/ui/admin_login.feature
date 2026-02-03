@admin
Feature: Admin Login & Dashboard Functionality
  As an admin
  I want to authenticate and access the dashboard
  So that I can manage the system securely

  @TC_UI_ADMIN_01
  Scenario: Successful Admin Login and Dashboard Redirection
    Given admin is on login page
    When admin enters username "admin" and password "admin123"
    And admin clicks login button
    Then admin should be redirected to dashboard
    And admin closes the browser

  @TC_UI_ADMIN_02
  Scenario: Invalid Username Error Display
    Given admin is on login page
    When admin enters username "invaliduser" and password "admin123"
    And admin clicks login button
    Then admin error message "Invalid username or password" should be displayed

  @TC_UI_ADMIN_03
  Scenario: Invalid Password Error Display
    Given admin is on login page
    When admin enters username "admin" and password "wrongpass"
    And admin clicks login button
    Then admin error message "Invalid username or password" should be displayed

  @TC_UI_ADMIN_04
  Scenario: Empty Username Field Validation
    Given admin is on login page
    When admin enters username "" and password "admin123"
    And admin clicks login button
    Then admin field error "Username is required" should be displayed

  @TC_UI_ADMIN_05
  Scenario: Empty Password Field Validation
    Given admin is on login page
    When admin enters username "admin" and password ""
    And admin clicks login button
    Then admin field error "Password is required" should be displayed

  @TC_UI_ADMIN_06
  Scenario: Both Username and Password Empty Validation
    Given admin is on login page
    When admin enters username "" and password ""
    And admin clicks login button
    Then admin field error "Username is required" should be displayed
    And admin field error "Password is required" should be displayed

  @TC_UI_ADMIN_07
  Scenario: Logout Success Message Display
    Given admin is logged in
    When admin clicks logout
    Then admin should see message "You have been logged out successfully"

  @TC_UI_ADMIN_08
  Scenario: Protected Page Access Without Login
    Given admin is not logged in
    When admin navigates to dashboard page
    Then admin should be redirected to login page

  @TC_UI_ADMIN_10
  Scenario: Initial Summary Cards Values
    Given admin is logged in
    Then admin dashboard summary cards should show initial values

  @TC_UI_ADMIN_11
  Scenario: Navigation Button Functionality for Admin
    Given admin is logged in
    When admin clicks Manage Categories button
    Then admin should be navigated to categories page

  @TC_UI_ADMIN_12
  Scenario: Active Dashboard Page Highlight
    Given admin is logged in
    Then admin dashboard menu item should be highlighted
