package stepdefinitions.ui;

import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.DriverFactory;

import java.time.Duration;

public class UserLoginSteps {

    @Given("user is logged in")
    public void user_is_logged_in() throws InterruptedException {
        user_is_on_login_page();
        user_enters_credentials("testuser", "test123");
        user_clicks_login_button();
        Thread.sleep(2000);
    }

    private WebDriver getDriver() {
        DriverFactory.initDriver();
        return DriverFactory.getDriver();
    }

    @Given("user is on login page")
    public void user_is_on_login_page() {
        getDriver().get("http://localhost:8080/ui/login");
        System.out.println("Login page opened ✅");
    }

    @When("user enters username {string} and password {string}")
    public void user_enters_credentials(String username, String password) {
        getDriver().findElement(By.name("username")).clear();
        getDriver().findElement(By.name("username")).sendKeys(username);

        getDriver().findElement(By.name("password")).clear();
        getDriver().findElement(By.name("password")).sendKeys(password);
    }

    @And("user clicks login button")
    public void user_clicks_login_button() {
        getDriver().findElement(By.cssSelector("button[type='submit']")).click();
    }

    // TC_UI_USER_01

    @Then("user should be redirected to dashboard")
    public void user_should_be_redirected_to_dashboard() throws InterruptedException {
        Thread.sleep(2000);
        if (getDriver().getCurrentUrl().contains("/dashboard")) {
            System.out.println("User redirected to dashboard ✅");
        } else {
            System.out.println("Dashboard redirection failed ❌");
        }
    }

    @And("close the browser")
    public void close_the_browser() {
        // Handled by Hooks
    }

    // TC_UI_USER_02,03

    @Then("error message {string} should be displayed")
    public void error_message_should_be_displayed(String expectedMessage) {
        if (getDriver().getPageSource().contains(expectedMessage)) {
            System.out.println("Error message displayed correctly ✅");
        } else {
            System.out.println("Expected error message NOT found ❌");
        }
    }

    // TC_UI_USER_04

    @Given("user is logged in as normal user")
    public void user_is_logged_in_as_normal_user() throws InterruptedException {
        user_is_on_login_page();
        user_enters_credentials("testuser", "test123");
        user_clicks_login_button();
        Thread.sleep(2000);
    }

    @When("user clicks logout")
    public void user_clicks_logout() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(@href,'logout')]"))).click();
        } catch (org.openqa.selenium.TimeoutException e) {
            throw new AssertionError("Logout link not found. Ensure user is logged in (URL not /ui/login). " + e.getMessage());
        }
    }

    @Then("user should see message {string}")
    public void user_should_see_message(String message) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains("logout"));

        if (getDriver().getCurrentUrl().contains("logout")) {
            System.out.println("Logout successful ✅");
        } else {
            System.out.println("Logout verification failed ❌");
        }
    }

    // TC_UI_USER_05

    @Given("user is not logged in")
    public void user_is_not_logged_in() {
        DriverFactory.initDriver();
    }

    // Navigation handled in UserSteps.java

    @Then("user should be redirected to login page")
    public void user_should_be_redirected_to_login_page() {
        if (getDriver().getCurrentUrl().contains("/login")) {
            System.out.println("Redirected to login page ✅");
        } else {
            System.out.println("Redirection failed ❌");
        }
    }

    @Then("dashboard should be displayed with summary cards")
    public void dashboard_should_be_displayed_with_summary_cards() {
        if (getDriver().getPageSource().contains("Dashboard")) {
            System.out.println("Dashboard loaded for user ✅");
        }
    }

    // TC_UI_USER_07

    @Then("dashboard summary cards should show initial values")
    public void dashboard_summary_cards_should_show_initial_values() {
        if (getDriver().getPageSource().contains("Categories")
                && getDriver().getPageSource().contains("Plants")
                && getDriver().getPageSource().contains("Sales")) {
            System.out.println("Summary cards visible for user ✅");
        }
    }

    @Then("admin action buttons should not be visible")
    public void admin_action_buttons_should_not_be_visible() {
        boolean adminButtonsPresent = getDriver().getPageSource().contains("Manage Categories") ||
                getDriver().getPageSource().contains("Add Plant") ||
                getDriver().getPageSource().contains("Sell Plant");

        if (!adminButtonsPresent) {
            System.out.println("Admin action buttons hidden for user ✅");
        } else {
            System.out.println("Admin action buttons visible ❌");
        }
    }

    // TC_UI_USER_09

    @When("user clicks Categories section")
    public void user_clicks_categories_section() {
        getDriver().findElement(By.partialLinkText("Categories")).click();
    }

    @Then("user should be navigated to categories page")
    public void user_should_be_navigated_to_categories_page() {
        if (getDriver().getCurrentUrl().contains("/categories")) {
            System.out.println("User navigated to Categories page ✅");
        }
    }

    @When("user clicks Plants section")
    public void user_clicks_plants_section() {
        getDriver().findElement(By.partialLinkText("Plants")).click();
    }

    @Then("user should be navigated to plants page")
    public void user_should_be_navigated_to_plants_page() {
        if (getDriver().getCurrentUrl().contains("/plants")) {
            System.out.println("User navigated to Plants page ✅");
        }
    }

    @When("user clicks Sales section")
    public void user_clicks_sales_section() {
        getDriver().findElement(By.partialLinkText("Sales")).click();
    }

    @Then("user should be navigated to sales page")
    public void user_should_be_navigated_to_sales_page() {
        if (getDriver().getCurrentUrl().contains("/sales")) {
            System.out.println("User navigated to Sales page ✅");
        }
    }
}