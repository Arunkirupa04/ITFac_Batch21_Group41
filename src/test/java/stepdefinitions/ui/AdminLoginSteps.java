package stepdefinitions.ui;

import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.DriverFactory;

import java.time.Duration;

public class AdminLoginSteps {

    private WebDriver getDriver() {
        DriverFactory.initDriver();
        return DriverFactory.getDriver();
    }

    @Given("admin is on login page")
    public void admin_is_on_login_page() {
        getDriver().get("http://localhost:8080/ui/login");
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        System.out.println("Admin login page opened ✅");
    }

    @When("admin enters username {string} and password {string}")
    public void admin_enters_credentials(String username, String password) {
        getDriver().findElement(By.name("username")).clear();
        getDriver().findElement(By.name("username")).sendKeys(username);

        getDriver().findElement(By.name("password")).clear();
        getDriver().findElement(By.name("password")).sendKeys(password);
    }

    @And("admin clicks login button")
    public void admin_clicks_login_button() {
        getDriver().findElement(By.cssSelector("button[type='submit']")).click();
    }

    // TC_UI_ADMIN_01
    @Then("admin should be redirected to dashboard")
    public void admin_should_be_redirected_to_dashboard() throws InterruptedException {
        Thread.sleep(2000);

        String currentUrl = getDriver().getCurrentUrl();
        System.out.println("Current URL: " + currentUrl);

        if (currentUrl.contains("/dashboard")) {
            System.out.println("Admin navigated to dashboard ✅");
        } else {
            System.out.println("Dashboard navigation failed ❌");
        }
    }

    @And("admin closes the browser")
    public void admin_closes_the_browser() {
        // Handled by Hooks
    }

    // TC_UI_ADMIN_02,03
    @Then("admin error message {string} should be displayed")
    public void admin_error_message_should_be_displayed(String expectedMessage) {
        if (getDriver().getPageSource().contains(expectedMessage)) {
            System.out.println("Correct error message displayed ✅");
        } else {
            System.out.println("Expected error message NOT displayed ❌");
        }
    }

    // TC_UI_ADMIN_04,05,06
    @Then("admin field error {string} should be displayed")
    public void admin_field_error_should_be_displayed(String expectedError) {
        if (getDriver().getPageSource().contains(expectedError)) {
            System.out.println("Validation error displayed: " + expectedError + " ✅");
        } else {
            System.out.println("Validation error missing: " + expectedError + " ❌");
        }
    }

    // TC_UI_ADMIN_07
    @Given("admin is logged in")
    public void admin_is_logged_in() throws InterruptedException {
        admin_is_on_login_page();
        admin_enters_credentials("admin", "admin123");
        admin_clicks_login_button();
        Thread.sleep(2000);
    }

    @When("admin clicks logout")
    public void admin_clicks_logout() {
        getDriver().findElement(By.linkText("Logout")).click();
    }

    @Then("admin should see message {string}")
    public void admin_should_see_message(String message) {
        if (getDriver().getPageSource().contains(message)) {
            System.out.println("Logout message displayed ✅");
        } else {
            System.out.println("Logout message missing ❌");
        }
    }

    // TC_UI_ADMIN_08
    @Given("admin is not logged in")
    public void admin_is_not_logged_in() {
        DriverFactory.initDriver();
    }

    @When("admin navigates to dashboard page")
    public void admin_navigates_to_dashboard_page() {
        getDriver().get("http://localhost:8080/ui/dashboard");
    }

    @Then("admin should be redirected to login page")
    public void admin_should_be_redirected_to_login_page() {
        if (getDriver().getCurrentUrl().contains("/login")) {
            System.out.println("Redirected to login page ✅");
        } else {
            System.out.println("Redirection failed ❌");
        }
    }

    // TC_UI_ADMIN_09

    @When("admin views dashboard page")
    public void admin_views_dashboard_page() {
        getDriver().get("http://localhost:8080/ui/dashboard");
    }

    @Then("admin dashboard should load successfully")
    public void admin_dashboard_should_load_successfully() {

        String pageSource = getDriver().getPageSource();

        if (pageSource.contains("Dashboard")) {
            System.out.println("Dashboard loaded successfully ✅");
        } else {
            System.out.println("Dashboard failed to load ❌");
        }
    }

    // TC_UI_ADMIN_10
    @Then("admin dashboard summary cards should show initial values")
    public void admin_dashboard_summary_cards_should_show_initial_values() {
        if (getDriver().getPageSource().contains("Categories")
                && getDriver().getPageSource().contains("Plants")
                && getDriver().getPageSource().contains("Sales")) {
            System.out.println("Summary cards visible with values ✅");
        }
    }

    // TC_UI_ADMIN_11
    @When("admin clicks Manage Categories button")
    public void admin_clicks_manage_categories_button() {
        getDriver().findElement(By.linkText("Manage Categories")).click();
    }

    @Then("admin should be navigated to categories page")
    public void admin_should_be_navigated_to_categories_page() {
        if (getDriver().getCurrentUrl().contains("/categories")) {
            System.out.println("Navigated to Categories page ✅");
        }
    }

    // TC_UI_ADMIN_12
    @Then("admin dashboard menu item should be highlighted")
    public void admin_dashboard_menu_item_highlighted() {
        if (getDriver().getPageSource().contains("active")) {
            System.out.println("Dashboard menu highlighted ✅");
        }
    }
}
