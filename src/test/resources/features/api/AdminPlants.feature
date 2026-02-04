@AdminAPI
Feature: Admin Plant Operations
  As an Admin
  I want to manage plants
  So that I can maintain the inventory

  Background:
    Given I am authenticated as an Admin

  @TC_API_ADMIN_22
  Scenario: Get Non-Existent Plant
    Given I try to get a plant with invalid id 999
    When I send a GET request to "/plants/999"
    Then the response status code should be 404
    And the error message should be "Not found"

  @TC_API_ADMIN_23
  Scenario: Update Plant Success
    Given I have a valid plant id to update
    And I provide valid update data with name "Updated", price 10.0, and quantity 5
    When I send a PUT request to update the plant
    Then the response status code should be 200
    And the response should match the updated plant data

  @TC_API_ADMIN_24
  Scenario: Invalid Update Data
    Given I have a valid plant id to update
    And I provide invalid update data with price -1
    When I send a PUT request to update the plant
    Then the response status code should be 400
    And a validation error should be returned

  @TC_API_ADMIN_25
  Scenario: Delete Plant
    Given I have a plant id 1 to delete
    When I send a DELETE request to delete the plant
    Then the response status code should be 200 or 204

  @TC_API_ADMIN_26
  Scenario: Get Plants by Category
    Given I have a category id 1
    When I send a GET request to "/plants/category/1"
    Then the response status code should be 200
    And the response should be a list of plants

  @TC_API_ADMIN_27
  Scenario: Create Plant under Category
    Given I have a category id 1
    And I provide plant data name "Rose", price 5.0, quantity 10
    When I send a POST request to create the plant in category 1
    Then the response status code should be 201
    And a new plant should be created

  @TC_API_ADMIN_28
  Scenario: Invalid Create Data
    Given I have a category id 1
    And I provide invalid plant data with quantity -1
    When I send a POST request to create the plant in category 1
    Then the response status code should be 400
    And a validation error should be returned
