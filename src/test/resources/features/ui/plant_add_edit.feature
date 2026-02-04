@ui @plants @admin
Feature: Add / Edit Plant - Admin

  Background:
    Given admin is logged in

  @TC_UI_ADMIN_41
  Scenario: Valid Plant Data Addition
    Given admin is on add plant page
    When admin enters plant name "Cactus"
    And admin enters plant price "10"
    And admin enters plant quantity "12"
    And admin selects category "Indoor"
    And admin clicks save plant
    Then plant should be added successfully

  @TC_UI_ADMIN_42
  Scenario: Valid Sub-Category Selection
    Given admin is on add plant page
    When admin selects sub category "Cacti"
    And admin clicks save plant
    Then sub category selection should be accepted

  @TC_UI_ADMIN_43
  Scenario: Cancel Redirect Check
    Given admin is on add plant page
    When admin clicks cancel on add plant page
    Then admin should be redirected to plants page

  @TC_UI_ADMIN_44
  Scenario: Empty Plant Name Validation
    Given admin is on add plant page
    When admin enters plant name ""
    And admin clicks save plant
    Then validation message "Plant name is required" should be shown

  @TC_UI_ADMIN_45
  Scenario: Short Plant Name Validation
    Given admin is on add plant page
    When admin enters plant name "Hi"
    And admin clicks save plant
    Then validation error should be shown for plant name

  @TC_UI_ADMIN_46
  Scenario: Long Plant Name Validation
    Given admin is on add plant page
    When admin enters plant name "VeryLongPlantNameMoreThanTwentyFive"
    And admin clicks save plant
    Then validation error should be shown for plant name

  @TC_UI_ADMIN_47
  Scenario: Empty Price Field Validation
    Given admin is on add plant page
    When admin enters plant price ""
    And admin clicks save plant
    Then validation message "Price is required" should be shown

  @TC_UI_ADMIN_48
  Scenario: Invalid Price Value Check
    Given admin is on add plant page
    When admin enters plant price "0"
    And admin clicks save plant
    Then validation message "Price must be > 0" should be shown

  @TC_UI_ADMIN_49
  Scenario: Empty Quantity Field Validation
    Given admin is on add plant page
    When admin enters plant quantity ""
    And admin clicks save plant
    Then validation message "Quantity is required" should be shown

  @TC_UI_ADMIN_50
  Scenario: Negative Quantity Value Check
    Given admin is on add plant page
    When admin enters plant quantity "-5"
    And admin clicks save plant
    Then validation error should be shown for quantity

  @TC_UI_ADMIN_51
  Scenario: Category Field Required Validation
    Given admin is on add plant page
    When admin selects category ""
    And admin clicks save plant
    Then validation message "Category is required" should be shown

  @TC_UI_ADMIN_52
  Scenario: Valid Plant Data Update
    Given a plant exists
    And admin is on edit plant page for "Rose"
    When admin updates plant price to "25"
    And admin clicks save plant
    Then plant should be updated successfully

  @TC_UI_ADMIN_53
  Scenario: Mandatory Fields Clearing Validation
    Given a plant exists
    And admin is on edit plant page for "Rose"
    When admin clears plant name
    And admin clicks save plant
    Then validation should be shown
