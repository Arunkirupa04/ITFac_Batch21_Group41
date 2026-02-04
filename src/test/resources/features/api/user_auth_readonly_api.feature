@UserAPI
Feature: User Authentication and Readonly Access
  As a standard user
  I want to log in and access categories safely
  So that I can only read data without performing restricted actions

  @TC_API_USER_01
  Scenario: Successful Test User Login
    Given user credentials username "testuser" and password "test123"
    When user sends POST request to "/api/auth/login"
    Then user response code should be 200
    And user JWT token should be generated

  @TC_API_USER_02
  Scenario: Get Existing Category - User
    Given user is logged in with JWT
    When user sends GET request to "/api/categories/1"
    Then response code should be 200
    And response JSON should contain "id":1

  @TC_API_USER_03
  Scenario: Unauthorized Access without JWT
    When user sends GET request without auth to "/api/categories/1"
    Then response code should be 401

  @TC_API_USER_04
  Scenario: Update Forbidden - User
    Given user is logged in with JWT
    When user sends PUT request to "/api/categories/1" with body:
      """
      {
        "name": "Try Update"
      }
      """
    Then response code should be 403
