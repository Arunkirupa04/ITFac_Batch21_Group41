@user @category @access_control
Feature: User Category Access Control
  As a User
  I should not be able to access admin-only pages
  System should block me with 403 Forbidden or redirect

  Background:
    Given User is logged in

  @TC_UI_USER_24
  Scenario: User Cannot Access Add Category URL
    When User attempts to access "/ui/categories/add"
    Then User should be blocked from accessing the page
    And User should not see Add Category form

  @TC_UI_USER_25
  Scenario: User Cannot Access Edit Category URL
    Given Category with ID 1 exists
    When User attempts to access "/ui/categories/edit/1"
    Then User should be blocked from accessing the page
    And User should not see Edit Category form
