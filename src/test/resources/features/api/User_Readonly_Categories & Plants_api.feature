@UserAPI @sachini
Feature: User Readonly Access for Categories & Plants

  Background:
    Given user is logged in with JWT

  @TC_API_USER_09
  Scenario: Retrieve all sub-categories for User
    When user sends GET request to "/api/categories/sub-categories"
    Then response code should be 200
    And the response should contain a list of sub-categories for user

  @TC_API_USER_10
  Scenario: Retrieve main categories only for User
    When user sends GET request to "/api/categories/main"
    Then response code should be 200
    And the response should contain a list of main categories for user

  @TC_API_USER_11
  Scenario: Retrieve plant by ID for User
    When user requests plant by id 1
    Then response code should be 200
    And the response should contain plant id 1 for user

  @TC_API_USER_12
  Scenario: Restrict update to Admin only for User (403)
    When user sends PUT request to "/api/plants/1" with body:
      """
      { "name": "UserTryUpdate", "price": 100, "quantity": 5, "categoryId": 1 }
      """
    Then response code should be 403

  @TC_API_USER_13
  Scenario: Restrict delete to Admin only for User (403)
    When user sends DELETE request to "/api/plants/1"
    Then response code should be 403