package stepdefinitions.ui;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.SalesPage;
import pages.SellPlantPage;
import utils.DriverFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminSalesSteps {

    private SalesPage salesPage;

    private void ensureDriver() {
        DriverFactory.initDriver();
    }

    private void loginAsAdmin() {
        ensureDriver();
        WebDriver driver = DriverFactory.getDriver();

        // Check if we are already logged in as admin by looking for specific admin-only
        // elements or current URL
        // However, the simplest way is to check the URL and if not on a protected page,
        // or if we want to be sure, go to login.
        driver.get("http://localhost:8080/ui/login");

        // If redirected away from login, we might be logged in.
        // Let's force a fresh login if we are not admin or if we are on dashboard but
        // not sure of role.
        if (!driver.getCurrentUrl().contains("/ui/login")) {
            // Assume we might be logged in as someone else or admin.
            // To be safe, logout if we need to ensure admin.
            driver.get("http://localhost:8080/ui/logout");
            driver.get("http://localhost:8080/ui/login");
        }

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(org.openqa.selenium.By.name("username")));

            if (driver.getCurrentUrl().contains("/ui/login")) {
                driver.findElement(org.openqa.selenium.By.name("username")).clear();
                driver.findElement(org.openqa.selenium.By.name("username")).sendKeys("admin");
                driver.findElement(org.openqa.selenium.By.name("password")).clear();
                driver.findElement(org.openqa.selenium.By.name("password")).sendKeys("admin123");
                driver.findElement(org.openqa.selenium.By.cssSelector("button[type='submit']")).click();

                // Wait for redirect away from login
                wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/ui/login")));
            }
        } catch (Exception e) {
            System.out.println("Login state check/process failed: " + e.getMessage());
        }
    }

    private SalesPage getSalesPage() {
        if (salesPage == null) {
            ensureDriver();
            salesPage = new SalesPage(DriverFactory.getDriver());
        }
        return salesPage;
    }

    @Given("admin is logged in and navigates to the sales page")
    public void admin_is_logged_in_and_navigates_to_the_sales_page() {
        loginAsAdmin();
        DriverFactory.getDriver().get("http://localhost:8080/ui/sales");
        // Wait for sales page header
        new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(10))
                .until(ExpectedConditions.textToBe(org.openqa.selenium.By.tagName("h3"), "Sales"));
    }

    @Given("the admin is viewing the sales list")
    public void the_admin_is_viewing_the_sales_list() {
        loginAsAdmin();
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
        // To accurately check and potentially create data, we SHOULD be admin.
        loginAsAdmin();

        if (getSalesPage().getSalesCount() <= 10) {
            System.out.println("Creating test sales data for pagination...");

            for (int i = 0; i < 11; i++) {
                DriverFactory.getDriver().get("http://localhost:8080/ui/sales/new");

                // If we are not on the sell page (e.g. Access Denied), loginAsAdmin might have
                // failed or session was lost
                if (!DriverFactory.getDriver().getCurrentUrl().contains("/ui/sales/new")) {
                    loginAsAdmin();
                    DriverFactory.getDriver().get("http://localhost:8080/ui/sales/new");
                }

                SellPlantPage sellPage = new SellPlantPage(DriverFactory.getDriver());
                String[] options = sellPage.getAllPlantOptions();
                if (options.length > 0) {
                    // Find first option that isn't a placeholder
                    String target = options[0];
                    for (String opt : options) {
                        if (!opt.toLowerCase().contains("select")) {
                            target = opt;
                            break;
                        }
                    }
                    sellPage.selectPlant(target);
                    sellPage.enterQuantity("1");
                    sellPage.clickSell();

                    // Small delay to ensure DB stays consistent during rapid writes
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                    }
                } else {
                    System.out.println("No plants available to create sales records.");
                    break;
                }
            }
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
        loginAsAdmin();
        DriverFactory.getDriver().get("http://localhost:8080/ui/sales");
    }

    @Then("a message {string} should be displayed")
    public void a_message_should_be_displayed(String expectedMessage) {
        Assert.assertEquals("Empty state message mismatch", expectedMessage, getSalesPage().getEmptyStateMessage());
    }
}
