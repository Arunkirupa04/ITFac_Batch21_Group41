@ui @plants @user
Feature: Plants Page - User

  Background:
    Given user is logged in
    And user navigates to plants page

  @TC_UI_USER_26
  Scenario: Plants Page Load for User
    Then plants page should load successfully

  @TC_UI_USER_27
  Scenario: User View Empty Plants List
    Given there are no plants in the database
    When user navigates to plants page
    Then no plants found message should be displayed

  @TC_UI_USER_28
  Scenario: User Cannot See Add Plant Button
    Then user should not see add plant button

  @TC_UI_USER_29
  Scenario: User Cannot See Edit Action for Plants
    Given plants exist
    Then user should not see edit action

  @TC_UI_USER_30
  Scenario: User Cannot See Delete Action for Plants
    Given plants exist
    Then user should not see delete action

  @TC_UI_USER_31
  Scenario: User Search Plants by Name
    Given multiple plants exist
    When search plant by name "Rose"
    Then only matching plant "Rose" should be shown

  @TC_UI_USER_32
  Scenario: User Filter Plants by Category
    Given plants exist in different categories
    When filter plants by category "Indoor"
    Then plants for category "Indoor" should be shown

  @TC_UI_USER_33
  Scenario: User Sort Plants by Name
    When sort plants by name
    Then plant list should be sorted

  @TC_UI_USER_34
  Scenario: User Sort Plants by Price
    When sort plants by price
    Then plant list should be sorted

  @TC_UI_USER_35
  Scenario: User Sort Plants by Stock
    When sort plants by stock
    Then plant list should be sorted

  @TC_UI_USER_36
  Scenario: User View Low Stock Badge
    Given a plant exists with stock less than 5
    Then low stock badge should be displayed

  @TC_UI_USER_37
  Scenario: User View Pagination for Plants
    Given plants exist more than 10
    Then pagination should be available
    When user clicks pagination page "2"
    Then plant list should update to the selected page
