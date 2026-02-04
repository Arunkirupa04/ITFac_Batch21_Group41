@UserAPI
Feature: User Sales Functional Restrictions
  As a standard user
  I want to access sales data according to my read-only permissions
  So that I cannot perform restricted actions like creating or deleting sales

  Background:
    Given user is authenticated via API

  @TC_API_USER_18
  Scenario: Verify 403 Forbidden when user attempts to create a sale
    When user sends a POST request to "/api/sales/plant/1" with quantity 1
    Then the response status code should be 403

  @TC_API_USER_19
  Scenario: Verify user can view the sales list
    When user sends a GET request to "/api/sales"
    Then the response status code should be 200
    And the response should contain a list of sales

  @TC_API_USER_20
  Scenario: Verify user can view a specific sale
    Given a sale exists with ID 1
    When user sends a GET request to retrieve the sale
    Then the response status code should be 200

  @TC_API_USER_21
  Scenario: Verify 403 Forbidden when user attempts to delete a sale
    Given a sale exists with ID 1
    When user sends a DELETE request to remove the sale
    Then the response status code should be 403
