@admin
Feature: Login Functionality

  @admin_login
  Scenario: Successful login with valid credentials
    Given user is on login page
    When user enters username "admin" and password "admin123"
    And user clicks login button
    Then user should be redirected to dashboard
    And close the browser
