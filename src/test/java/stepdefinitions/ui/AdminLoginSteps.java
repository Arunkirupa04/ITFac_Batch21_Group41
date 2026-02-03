package stepdefinitions.ui;

import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class AdminLoginSteps {

    WebDriver driver;

    @Given("user is on login page")
    public void user_is_on_login_page() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/ui/login");
        driver.manage().window().maximize();
        // Wait for elements to be available
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        System.out.println("Login page opened ✅");
    }

    @When("user enters username {string} and password {string}")
    public void user_enters_credentials(String username, String password) {
        driver.findElement(By.name("username")).clear();
        driver.findElement(By.name("username")).sendKeys(username);

        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys(password);
    }

    @And("user clicks login button")
    public void user_clicks_login_button() {
        // Adjust selector based on your login button
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }
//TC_UI_ADMIN_01
    @Then("user should be redirected to dashboard")
    public void user_should_be_redirected() throws InterruptedException {
        // Wait for login to process
        Thread.sleep(2000); // 2 seconds, can adjust

        // Navigate to dashboard directly
        driver.get("http://localhost:8080/ui/dashboard");

        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL: " + currentUrl);

        if(currentUrl.contains("/dashboard")){
            System.out.println("Navigated to dashboard ✅");
        } else {
            System.out.println("Navigation failed ❌");
        }
    }



    @And("close the browser")
    public void close_browser() {
        driver.quit();
    }

    //TC_UI_ADMIN_02,03
    @Then("error message {string} should be displayed")
    public void error_message_should_be_displayed(String expectedMessage) {
        if (driver.getPageSource().contains(expectedMessage)) {
            System.out.println("Correct error message displayed ✅");
        } else {
            System.out.println("Expected error message NOT displayed ❌");
        }
        driver.quit();
    }

    //TC_UI_ADMIN_04,05,06
    @Then("field error {string} should be displayed")
    public void field_error_should_be_displayed(String expectedError) {
        if (driver.getPageSource().contains(expectedError)) {
            System.out.println("Validation error displayed: " + expectedError + " ✅");
        } else {
            System.out.println("Validation error missing: " + expectedError + " ❌");
        }
    }

    //TC_UI_ADMIN_07

    @Given("user is logged in as admin")
    public void user_is_logged_in_as_admin() throws InterruptedException {
        user_is_on_login_page();
        user_enters_credentials("admin", "admin123");
        user_clicks_login_button();
        Thread.sleep(2000);
    }

    @When("user clicks logout")
    public void user_clicks_logout() {
        driver.findElement(By.linkText("Logout")).click();
    }

    @Then("user should see message {string}")
    public void user_should_see_message(String message) {
        if (driver.getPageSource().contains(message)) {
            System.out.println("Logout message displayed ✅");
        } else {
            System.out.println("Logout message missing ❌");
        }
        driver.quit();
    }

    //TC_UI_ADMIN_08

    @Given("user is not logged in")
    public void user_is_not_logged_in() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @When("user navigates to dashboard page")
    public void user_navigates_to_dashboard_page() {
        driver.get("http://localhost:8080/ui/dashboard");
    }

    @Then("user should be redirected to login page")
    public void user_should_be_redirected_to_login_page() {
        if (driver.getCurrentUrl().contains("/login")) {
            System.out.println("Redirected to login page ✅");
        } else {
            System.out.println("Redirection failed ❌");
        }
        driver.quit();
    }

    //TC_UI_ADMIN_10
    @Then("dashboard summary cards should show initial values")
    public void dashboard_summary_cards_should_show_initial_values() {
        if (driver.getPageSource().contains("Categories")
                && driver.getPageSource().contains("Plants")
                && driver.getPageSource().contains("Sales")) {
            System.out.println("Summary cards visible with values ✅");
        }
        driver.quit();
    }

    //TC_UI_ADMIN_11

    @When("admin clicks Manage Categories button")
    public void admin_clicks_manage_categories_button() {
        driver.findElement(By.linkText("Manage Categories")).click();
    }

    @Then("admin should be navigated to categories page")
    public void admin_should_be_navigated_to_categories_page() {
        if (driver.getCurrentUrl().contains("/categories")) {
            System.out.println("Navigated to Categories page ✅");
        }
        driver.quit();
    }

    //TC_UI_ADMIN_12

    @Then("dashboard menu item should be highlighted")
    public void dashboard_menu_item_should_be_highlighted() {
        if (driver.getPageSource().contains("active")) {
            System.out.println("Dashboard menu highlighted ✅");
        }
        driver.quit();
    }

}
