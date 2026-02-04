@ui @plants @admin
Feature: Plants Page - Admin

  Background:
    Given admin is logged in
    And admin navigates to plants page

  @TC_UI_ADMIN_30
  Scenario: Admin Plant List View Loading
    Then plants page should load successfully

  @TC_UI_ADMIN_31
  Scenario: Plant List Pagination Functionality
    Given plants exist more than 10
    Then pagination should be available
    When user clicks pagination page "2"
    Then plant list should update to the selected page

  @TC_UI_ADMIN_32
  Scenario: Search by Plant Name
    When search plant by name "Rose"
    Then only matching plant "Rose" should be shown

  @TC_UI_ADMIN_33
  Scenario: Plant Category Filter
    Given categories exist
    When filter plants by category "Indoor"
    Then plants for category "Indoor" should be shown

  @TC_UI_ADMIN_34
  Scenario: Sorting Plant List by Name
    When sort plants by name
    Then plant list should be sorted

  @TC_UI_ADMIN_35
  Scenario: Sorting Plant List by Sub Category
    # NOTE: This will work only if Category/Sub Category header has sort link on UI
    When sort plants by category
    Then plant list should be sorted

  @TC_UI_ADMIN_36
  Scenario: Sorting Plant List by Price
    When sort plants by price
    Then plant list should be sorted

  @TC_UI_ADMIN_37
  Scenario: Sorting Plant List by Stock
    When sort plants by stock
    Then plant list should be sorted

  @TC_UI_ADMIN_38
  Scenario: Low Stock Quantity Indicator Badge
    Given a plant exists with stock less than 5
    Then low stock badge should be displayed

  @TC_UI_ADMIN_39
  Scenario: Empty Plant List UI Check
    Given there are no plants in the database
    When admin navigates to plants page
    Then no plants found message should be displayed

  @TC_UI_ADMIN_40
  Scenario: Admin Actions Visibility Check
    Then admin should see add edit and delete actions
