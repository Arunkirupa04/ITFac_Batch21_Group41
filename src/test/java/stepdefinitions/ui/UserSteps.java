package stepdefinitions.ui;

import io.cucumber.java.en.When;
import utils.DriverFactory;
import pages.SalesPage;

public class UserSteps {

    private SalesPage salesPage;

    // Reuse AdminSalesSteps for generic actions if reachable,
    // or just reimplement simple calls to SalesPage since we shouldn't
    // cross-pollenate step classes if they have state.
    // It's safer to use SalesPage directly here.

    private void ensureDriver() {
        DriverFactory.initDriver();
    }

    private SalesPage getSalesPage() {
        if (salesPage == null) {
            ensureDriver();
            salesPage = new SalesPage(DriverFactory.getDriver());
        }
        return salesPage;
    }

    @When("user navigates to {string}")
    public void user_navigates_to(String urlPath) {
        ensureDriver();
        String targetUrl = urlPath;
        if (urlPath.startsWith("ui/"))
            targetUrl = "/" + urlPath;
        else if (!urlPath.startsWith("/"))
            targetUrl = "/ui/" + urlPath.toLowerCase();

        DriverFactory.getDriver().get("http://localhost:8080" + targetUrl);
    }

    @When("user navigates to the Dashboard")
    @When("user navigates to dashboard page")
    public void user_navigates_to_dashboard_alias() {
        user_navigates_to("/ui/dashboard");
    }

    @When("user clicks on {string} column header")
    public void user_clicks_on_column_header(String columnName) {
        getSalesPage().clickColumnHeader(columnName);
    }

    @When("user clicks on Next page button")
    public void user_clicks_on_next_page_button() {
        getSalesPage().clickNextPage();
    }

    @When("the user is on the sales page")
    public void user_is_on_the_sales_page() {
        ensureDriver();
        if (!getSalesPage().isOnSalesPage()) {
            DriverFactory.getDriver().get("http://localhost:8080/ui/sales");
        }
    }

    // Reusing Given steps from other classes isn't standard Cucumber across classes
    // unless package scan picks them up.
    // AdminSellSteps has "a non-admin user is logged in" which we need.
    // If AdminSellSteps is in the glue path, we can reuse it in feature file.
    // Ensure AdminSellSteps.java is in the same package `stepdefinitions.ui`.
}
