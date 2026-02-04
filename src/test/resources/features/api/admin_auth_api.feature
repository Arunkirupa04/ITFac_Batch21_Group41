@AdminAPI
Feature: Admin Authentication and Authorization APIs
  As an admin
  I want to authenticate and obtain secure access to the system
  So that I can safely perform administrative API operations

  @TC_API_ADMIN_01
  Scenario: Successful Admin Login and JWT retrieval
    Given admin credentials username "admin" and password "admin123"
    When admin sends POST request to "/api/auth/login"
    Then response code should be 200
    And JWT token should be generated

  @TC_API_ADMIN_02
  Scenario: Invalid username for Admin login
    Given admin credentials username "invalid" and password "admin123"
    When admin sends POST request to "/api/auth/login"
    Then response code should be 401
    And response message should contain "Unauthorized"

  @TC_API_ADMIN_03
  Scenario: Invalid password for Admin login
    Given admin credentials username "admin" and password "wrong"
    When admin sends POST request to "/api/auth/login"
    Then response code should be 401
    And response message should contain "Unauthorized"

  @TC_API_ADMIN_04
  Scenario: Empty username field
    Given admin credentials username "" and password "admin123"
    When admin sends POST request to "/api/auth/login"
    Then response code should be 401
    And response should contain validation error

  @TC_API_ADMIN_05
  Scenario: Empty password field
    Given admin credentials username "admin" and password ""
    When admin sends POST request to "/api/auth/login"
    Then response code should be 401
    And response should contain validation error

  @TC_API_ADMIN_06
  Scenario: Malformed JSON in login request
    Given admin sends malformed JSON body
    When admin sends POST request to "/api/auth/login"
    Then response code should be 500
    And response message should contain "JSON parse error"

  @TC_API_ADMIN_07
  Scenario: Retrieve existing category by ID as Admin
    Given admin is logged in with JWT
    When admin sends GET request to "/api/categories/1"
    Then response code should be 200
    And response JSON should contain "id":1
