
  @UserSales @user
  Feature: User Sales Page Functionality
  As a standard user
  I want to view the sales list
  So that I can see sales data without modification access

  @TC_UI_USER_40
  Scenario: User Sales Page Load
    Given a non-admin user is logged in
    And the user is on the sales page
    Then the sales page should load successfully with heading "Sales"
    And sales data table should be displayed

  @TC_UI_USER_41
  Scenario: User Empty Sales List
    Given a non-admin user is logged in
    And there are no sales records
    When the user is on the sales page
    Then a message "No sales found" should be displayed

  @TC_UI_USER_42
  Scenario: User Cannot See Sell Plant Button
    Given a non-admin user is logged in
    And the user is on the sales page
    Then the "Sell Plant" button should NOT be visible

  @TC_UI_USER_43
  Scenario: User Cannot See Delete Action
    Given a non-admin user is logged in
    And the user is on the sales page
    And at least one sale exists in the system
    Then delete button should not be visible

  @TC_UI_USER_44
  Scenario: User View Sales List with Pagination
    Given there are more than 10 sales records
    And a non-admin user is logged in
    And the user is on the sales page
    Then pagination controls should be visible

  @TC_UI_USER_45
  Scenario: User Sort Sales by Plant Name
    Given a non-admin user is logged in
    And the user is on the sales page
    When user clicks on "Plant" column header
    Then sales should be sorted alphabetically by plant name

  @TC_UI_USER_46
  Scenario: User Sort Sales by Quantity
    Given a non-admin user is logged in
    And the user is on the sales page
    When user clicks on "Quantity" column header
    Then sales should be sorted by quantity in ascending order

  @TC_UI_USER_47
  Scenario: User Sort Sales by Total Price
    Given a non-admin user is logged in
    And the user is on the sales page
    When user clicks on "Total Price" column header
    Then sales should be sorted by total price in ascending order

  @TC_UI_USER_48
  Scenario: User Sort Sales by Sold Date
    Given a non-admin user is logged in
    And the user is on the sales page
    When user clicks on "Sold At" column header
    Then sales should be sorted by date in ascending order

  @TC_UI_USER_49
  Scenario: User Pagination Next Page
    Given there are more than 10 sales records
    And a non-admin user is logged in
    And the user is on the sales page
    When user clicks on Next page button
    Then the next set of records should be displayed

#  @TC_UI_USER_50
#  Scenario: User Empty Sales List Message
#    Given a non-admin user is logged in
#    And there are no sales records
#    When the user is on the sales page
#    Then a message "No sales found" should be displayed
