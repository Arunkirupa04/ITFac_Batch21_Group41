@UserAPI
Feature: User Plant Operations
  As a User
  I want to view plants
  So that I can see available inventory

  Background:
    Given I am authenticated as a User

  @TC_API_USER_14
  Scenario: Get Plants by Category
    Given I have a category id 1
    When I send a GET request to "/plants/category/1"
    Then the response status code should be 200
    And the response should be a list of plants

  @TC_API_USER_15
  Scenario: Create Plant Forbidden
    Given I have a category id 1
    And I provide plant data name "Rose", price 5.0, quantity 10
    When I send a POST request to create the plant in category 1
    Then the response status code should be 403

  @TC_API_USER_16
  Scenario: Get All Plants
    When I send a GET request to "/plants"
    Then the response status code should be 200
    And the response should be a list of plants

  @TC_API_USER_17
  Scenario: Get Plant Summary
    When I send a GET request to "/plants/summary"
    Then the response status code should be 200
    And the response should match the plant summary structure
