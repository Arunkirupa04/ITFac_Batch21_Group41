@user @api @category_api
Feature: User Category API Access Control

  Background:
    Given User is authenticated with valid JWT token

  @TC_API_USER_05
  Scenario: DELETE forbidden for User
    Given Category with id 1 exists via API
    When User sends DELETE request to "/api/categories/1"
    Then Response status should be 403
    And Response should contain error "Forbidden"

  @TC_API_USER_06
  Scenario: GET all categories (read-only)
    Given Multiple categories exist in database via API
    When User sends GET request to "/api/categories"
    Then Response status should be 200
    And Response should contain array of categories

  @TC_API_USER_07
  Scenario: POST create forbidden for User
    When User sends POST request to "/api/categories" with body:
      | name     |
      | TestCat1 |
    Then Response status should be 403
    And Response should contain error "Forbidden"

  @TC_API_USER_08
  Scenario: GET category summary (read-only)
    When User sends GET request to "/api/categories/summary"
    Then Response status should be 200
    And Response should contain summary data
