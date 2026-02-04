@UserAccess
Feature: User Access Control
  As a standard user
  I want to be restricted from admin-only pages
  So that I cannot modify system data unauthorized

  @TC_UI_USER_38
  Scenario: User Cannot Access Add Plant Page
    Given a non-admin user is logged in
    When user navigates to "/ui/plants/add"
    Then user should be redirected to 403 forbidden page

  @TC_UI_USER_39
  Scenario: User Cannot Access Edit Plant Page
    Given a non-admin user is logged in
    When user navigates to "/ui/plants/edit/1"
    Then user should be redirected to 403 forbidden page
