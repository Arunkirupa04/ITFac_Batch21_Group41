@AdminAPI @sachini
Feature: Admin Category & Plant APIs

  Background:
    Given admin is authenticated via API

  @TC_API_ADMIN_15
  Scenario: Validate invalid create data for Admin
    When admin sends POST request to "/api/categories" with body:
      """
      { "name": "" }
      """
    Then response code should be 400
    And response should contain validation error

  @TC_API_ADMIN_16
  Scenario: Retrieve category summary for Admin
    When admin sends a GET request to "/api/categories/summary"
    Then response code should be 200
    And the response should contain a category summary DTO

  @TC_API_ADMIN_17
  Scenario: Retrieve all sub-categories for Admin
    When admin sends a GET request to "/api/categories/sub-categories"
    Then response code should be 200
    And the response should contain a list of sub-categories

  @TC_API_ADMIN_18
  Scenario: Paginated search by name/parent with sorting for Admin
    When admin sends GET request to "/api/categories/page" with parameters:
      | page | 0 |
      | size | 10 |
      | sort | name,asc |
      | name | Flowers |
    Then response code should be 200
    And the response should contain a paged categories response

  @TC_API_ADMIN_19
  Scenario: Handle invalid parameters for Admin paginated search
    When admin sends GET request to "/api/categories/page" with parameters:
      | page | -1 |
    Then response code should be 400
    And response should contain validation error

  @TC_API_ADMIN_20
  Scenario: Retrieve main categories only for Admin
    When admin sends a GET request to "/api/categories/main"
    Then response code should be 200
    And the response should contain a list of main categories

  @TC_API_ADMIN_21
  Scenario: Retrieve plant by ID for Admin
    When admin requests plant by id 1
    Then response code should be 200
    And the response should contain plant id 1