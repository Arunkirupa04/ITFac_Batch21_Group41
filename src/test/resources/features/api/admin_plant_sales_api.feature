@AdminAPI
Feature: Admin Plant and Sales Management APIs
  As an admin
  I want to manage plants and sales through secured APIs
  So that inventory and sales data remain accurate and consistent

  Background:
    Given admin is authenticated via API

  @TC_API_ADMIN_29
  Scenario: Verify all plants list as Admin
    When admin sends a GET request to "/api/plants"
    Then the response status code should be 200
    And the response should contain a list of plants

  @TC_API_ADMIN_30
  Scenario: Verify plant stats as Admin
    When admin sends a GET request to "/api/plants/summary"
    Then the response status code should be 200
    And the response should contain a summary of plants

  @TC_API_ADMIN_31
  Scenario: Verify paged and sorted plants as Admin
    When admin sends a GET request to "/api/plants/paged" with parameters:
      | page | 0 |
      | size | 10 |
      | sort | price,asc |
    Then the response status code should be 200
    And the response should contain paginated plant data

  @TC_API_ADMIN_32
  Scenario: Verify admin can sell plant and update inventory
    Given a plant exists with ID 1 and sufficient stock
    When admin sends a POST request to "/api/sales/plant/1" with body:
      """
      {
        "quantity": 2
      }
      """
    Then the response status code should be 201
    And the sale should be created and inventory reduced

  @TC_API_ADMIN_33
  Scenario: Verify error if quantity exceeds stock as Admin
    Given a plant exists with ID 1 and limited stock
    When admin sends a POST request to "/api/sales/plant/1" with quantity more than available stock
    Then the response status code should be 400
    And the response should contain error "Insufficient stock"

  @TC_API_ADMIN_34
  Scenario: Verify sales list as Admin
    When admin sends a GET request to "/api/sales"
    Then the response status code should be 200
    And the response should contain a list of sales

  @TC_API_ADMIN_35
  Scenario: Verify retrieval of an existing sale as Admin
    Given a sale exists with ID 1
    When admin sends a GET request to "/api/sales/1"
    Then the response status code should be 200
    And the response should contain the details of sale ID 1

  @TC_API_ADMIN_36
  Scenario: Verify sale deletion as Admin
    Given a sale exists with ID 1
    When admin sends a DELETE request to "/api/sales/1"
    Then the response status code should be 200 or 204
    And the sale with ID 1 should no longer exist
