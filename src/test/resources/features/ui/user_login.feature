@user
Feature: User Login & Dashboard Functionality
  As a normal user
  I want to login and access the dashboard
  So that I can view system information and navigate safely

  @TC_UI_USER_01
  Scenario: Successful User Login and Dashboard Redirection
    Given user is on login page
    When user enters username "testuser" and password "test123"
    And user clicks login button
    Then user should be redirected to dashboard
    And close the browser

  @TC_UI_USER_02
  Scenario: Invalid Username Error Display for User
    Given user is on login page
    When user enters username "invaliduser" and password "user123"
    And user clicks login button
    Then error message "Invalid username or password" should be displayed

  @TC_UI_USER_03
  Scenario: Invalid Password Error Display for User
    Given user is on login page
    When user enters username "testuser" and password "wrongpass"
    And user clicks login button
    Then error message "Invalid username or password" should be displayed

  @TC_UI_USER_04
  Scenario: User Logout Success Message
    Given user is logged in as normal user
    When user clicks logout
    Then user should see message "You have been logged out successfully"

  @TC_UI_USER_05
  Scenario: User Direct Access Without Login Redirection
    Given user is not logged in
    When user navigates to dashboard page
    Then user should be redirected to login page

  @TC_UI_USER_06
  Scenario: Dashboard Load After User Login
    Given user is logged in as normal user
    Then dashboard should be displayed with summary cards

  @TC_UI_USER_07
  Scenario: Summary Cards Display Values for User
    Given user is logged in as normal user
    Then dashboard summary cards should show initial values

  @TC_UI_USER_08
  Scenario: Admin Buttons Hidden for User
    Given user is logged in as normal user
    Then admin action buttons should not be visible

  @TC_UI_USER_09
  Scenario: User Navigation to Categories from Dashboard
    Given user is logged in as normal user
    When user clicks Categories section
    Then user should be navigated to categories page

  @TC_UI_USER_10
  Scenario: User Navigation to Plants from Dashboard
    Given user is logged in as normal user
    When user clicks Plants section
    Then user should be navigated to plants page

  @TC_UI_USER_11
  Scenario: User Navigation to Sales from Dashboard
    Given user is logged in as normal user
    When user clicks Sales section
    Then user should be navigated to sales page
