@AdminSales @admin
Feature: Admin Sales Page Verification
  As an admin
  I want to view the sales list
  So that I can monitor sales data, sort records, and navigate through pages

  @TC_UI_ADMIN_54
  Scenario: Admin View Sales List with Pagination
    Given admin is logged in and navigates to the sales page
    And there are more than 10 sales records
    Then the sales page should load successfully with heading "Sales"
    And sales data table should be displayed
    And pagination controls should be visible

  @TC_UI_ADMIN_55
  Scenario: Sorting Sales by Plant Name
    Given the admin is viewing the sales list
    When admin clicks on "Plant" column header
    Then sales should be sorted alphabetically by plant name

  @TC_UI_ADMIN_56
  Scenario: Sorting Sales by Quantity
    Given the admin is viewing the sales list
    When admin clicks on "Quantity" column header
    Then sales should be sorted by quantity in ascending order

  @TC_UI_ADMIN_57
  Scenario: Sorting Sales by Total Price
    Given the admin is viewing the sales list
    When admin clicks on "Total Price" column header
    Then sales should be sorted by total price in ascending order

  @TC_UI_ADMIN_58
  Scenario: Sorting Sales by Sold Date
    Given the admin is viewing the sales list
    When admin clicks on "Sold At" column header
    Then sales should be sorted by date in ascending order

  @TC_UI_ADMIN_59
  Scenario: Pagination Next Page Navigation
    Given the admin is viewing the sales list
    And there are more than 10 sales records
    When admin clicks on Next page button
    Then the next set of records should be displayed

  @TC_UI_ADMIN_60
  Scenario: Empty Sales List Message
    Given there are no sales records
    When admin navigates to the sales page
    Then a message "No sales found" should be displayed
