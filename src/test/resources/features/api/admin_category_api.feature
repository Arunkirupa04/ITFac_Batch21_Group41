@admin @api @category_api
Feature: Admin Category API Management

  Background:
    Given Admin is authenticated with valid JWT token

  @TC_API_ADMIN_08
  Scenario: GET non-existent category ID
    When Admin sends GET request to "/api/categories/999"
    Then Response status should be 404
    And Response should contain error message "Category not found"

  @TC_API_ADMIN_09
  Scenario: PUT update category details
    Given A category exists for update
    When Admin sends PUT request to that category with body:
      | name      |
      | UpdFlower |
    Then Response status should be 200
    And Response should contain updated category name "UpdFlower"

  @TC_API_ADMIN_10
  Scenario: PUT with invalid data validation
    Given A category exists for update
    When Admin sends PUT request to that category with body:
      | name |
      |      |
    Then Response status should be 400
    And Response should contain validation error

  @TC_API_ADMIN_11
  Scenario: DELETE category by ID
    Given Category with id for deletion exists
    When Admin sends DELETE request to that category
    Then Response status should be 200 or 204
    And Category should be removed from database

  @TC_API_ADMIN_12
  Scenario: GET all categories
    Given Multiple categories exist in database via API
    When Admin sends GET request to "/api/categories"
    Then Response status should be 200
    And Response should contain array of categories

  @TC_API_ADMIN_13
  Scenario: POST create main category
    When Admin sends POST request to "/api/categories" with body:
      | name    | parentCategory |
      | NewCat1 | null           |
    Then Response status should be 201
    And Response should contain new category ID

  @TC_API_ADMIN_14
  Scenario: POST create sub-category
    Given Parent category exists for sub-category
    When Admin sends POST request to "/api/categories" with sub-category body:
      | name    |
      | SubCat1 |
    Then Response status should be 201
    And Response should contain new sub-category with parent
