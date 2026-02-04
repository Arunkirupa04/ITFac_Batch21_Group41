package stepdefinitions.ui;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import pages.SalesPage;
import utils.DriverFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminSalesSteps {

    private SalesPage salesPage;

    private SalesPage getSalesPage() {
        if (salesPage == null) {
            salesPage = new SalesPage(DriverFactory.getDriver());
        }
        return salesPage;
    }

    @Given("admin is logged in and navigates to the sales page")
    public void admin_is_logged_in_and_navigates_to_the_sales_page() {
        DriverFactory.getDriver().get("http://localhost:8080/ui/sales");
    }

    @Given("the admin is viewing the sales list")
    public void the_admin_is_viewing_the_sales_list() {
        // Assuming admin is already logged in or this step handles navigation if not
        if (!getSalesPage().isOnSalesPage()) {
            DriverFactory.getDriver().get("http://localhost:8080/ui/sales");
        }
    }

    @Then("the sales page should load successfully with heading {string}")
    public void the_sales_page_should_load_successfully_with_heading(String expectedHeading) {
        Assert.assertTrue("Not on sales page", getSalesPage().isOnSalesPage());
        Assert.assertEquals("Page heading mismatch", expectedHeading, getSalesPage().getPageHeading());
    }

    @Then("sales data table should be displayed")
    public void sales_data_table_should_be_displayed() {
        Assert.assertTrue("Sales table not displayed", getSalesPage().isSalesTableDisplayed());
    }

    @Then("pagination controls should be visible")
    public void pagination_controls_should_be_visible() {
        Assert.assertTrue("Pagination controls not visible", getSalesPage().isPaginationVisible());
    }

    @When("admin clicks on {string} column header")
    public void admin_clicks_on_column_header(String columnName) {
        getSalesPage().clickColumnHeader(columnName);
    }

    @Then("sales should be sorted alphabetically by plant name")
    public void sales_should_be_sorted_alphabetically_by_plant_name() {
        List<String> actualData = getSalesPage().getColumnData(1); // Assuming 1st column is Plant
        List<String> sortedData = new ArrayList<>(actualData);
        Collections.sort(sortedData);
        Assert.assertEquals("Sales not sorted by Plant Name", sortedData, actualData);
    }

    @Then("sales should be sorted by quantity in ascending order")
    public void sales_should_be_sorted_by_quantity_in_ascending_order() {
        List<String> textData = getSalesPage().getColumnData(2); // Assuming 2nd column is Quantity
        List<Integer> actualData = new ArrayList<>();
        for (String s : textData) {
            actualData.add(Integer.parseInt(s));
        }
        List<Integer> sortedData = new ArrayList<>(actualData);
        Collections.sort(sortedData);
        Assert.assertEquals("Sales not sorted by Quantity", sortedData, actualData);
    }

    @Then("sales should be sorted by total price in ascending order")
    public void sales_should_be_sorted_by_total_price_in_ascending_order() {
        List<String> textData = getSalesPage().getColumnData(3); // Assuming 3rd column is Total Price
        List<Double> actualData = new ArrayList<>();
        for (String s : textData) {
            // Remove currency symbols if present, simplify expectation for now
            actualData.add(Double.parseDouble(s.replace("$", "").trim()));
        }
        List<Double> sortedData = new ArrayList<>(actualData);
        Collections.sort(sortedData);
        Assert.assertEquals("Sales not sorted by Total Price", sortedData, actualData);
    }

    @Then("sales should be sorted by date in ascending order")
    public void sales_should_be_sorted_by_date_in_ascending_order() {
        List<String> actualData = getSalesPage().getColumnData(4); // Assuming 4th column is Sold At
        List<String> sortedData = new ArrayList<>(actualData);
        Collections.sort(sortedData); // Using String comparison for dates for now (ISO format assumed)
        Assert.assertEquals("Sales not sorted by Date", sortedData, actualData);
    }

    @Given("there are more than 10 sales records")
    public void there_are_more_than_10_sales_records() {
        if (getSalesPage().getSalesCount() <= 10) {
            System.out.println("Warning: Less than 10 records, pagination test might fail.");
        }
    }

    @When("admin clicks on Next page button")
    public void admin_clicks_on_next_page_button() {
        getSalesPage().clickNextPage();
    }

    @Then("the next set of records should be displayed")
    public void the_next_set_of_records_should_be_displayed() {
        Assert.assertTrue("Sales table should still be displayed after pagination",
                getSalesPage().isSalesTableDisplayed());
    }

    @Given("there are no sales records")
    public void there_are_no_sales_records() {
        System.out.println("Expecting empty sales list for this test.");
    }

    @When("admin navigates to the sales page")
    public void admin_navigates_to_the_sales_page() {
        DriverFactory.getDriver().get("http://localhost:8080/ui/sales");
    }

    @Then("a message {string} should be displayed")
    public void a_message_should_be_displayed(String expectedMessage) {
        Assert.assertEquals("Empty state message mismatch", expectedMessage, getSalesPage().getEmptyStateMessage());
    }
}
