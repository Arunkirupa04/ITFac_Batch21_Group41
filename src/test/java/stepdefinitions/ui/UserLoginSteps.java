package stepdefinitions.ui;

import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class UserLoginSteps {

    WebDriver driver;



    @Given("user is on login page")
    public void user_is_on_login_page() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("http://localhost:8080/ui/login");
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
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    // TC_UI_USER_01

    @Then("user should be redirected to dashboard")
    public void user_should_be_redirected_to_dashboard() throws InterruptedException {
        Thread.sleep(2000);
        if (driver.getCurrentUrl().contains("/dashboard")) {
            System.out.println("User redirected to dashboard ✅");
        } else {
            System.out.println("Dashboard redirection failed ❌");
        }
    }

    @And("close the browser")
    public void close_the_browser() {
        driver.quit();
    }

    // TC_UI_USER_02,03

    @Then("error message {string} should be displayed")
    public void error_message_should_be_displayed(String expectedMessage) {
        if (driver.getPageSource().contains(expectedMessage)) {
            System.out.println("Error message displayed correctly ✅");
        } else {
            System.out.println("Expected error message NOT found ❌");
        }
        driver.quit();
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

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href,'logout')]")
        )).click();
    }


    @Then("user should see message {string}")
    public void user_should_see_message(String message) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        wait.until(ExpectedConditions.urlContains("logout"));

        if (driver.getCurrentUrl().contains("logout")) {
            System.out.println("Logout successful ✅");
        } else {
            System.out.println("Logout verification failed ❌");
        }

        driver.quit();
    }



    //TC_UI_USER_05

    @Given("user is not logged in")
    public void user_is_not_logged_in() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
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

    // TC_UI_USER_06

    @Then("dashboard should be displayed with summary cards")
    public void dashboard_should_be_displayed_with_summary_cards() {
        if (driver.getPageSource().contains("Dashboard")) {
            System.out.println("Dashboard loaded for user ✅");
        }
        driver.quit();
    }

    // TC_UI_USER_07

    @Then("dashboard summary cards should show initial values")
    public void dashboard_summary_cards_should_show_initial_values() {
        if (driver.getPageSource().contains("Categories")
                && driver.getPageSource().contains("Plants")
                && driver.getPageSource().contains("Sales")) {
            System.out.println("Summary cards visible for user ✅");
        }
        driver.quit();
    }

    // TC_UI_USER_08

    @Then("admin action buttons should not be visible")
    public void admin_action_buttons_should_not_be_visible() {
        boolean adminButtonsPresent =
                driver.getPageSource().contains("Manage Categories") ||
                        driver.getPageSource().contains("Add Plant") ||
                        driver.getPageSource().contains("Sell Plant");

        if (!adminButtonsPresent) {
            System.out.println("Admin action buttons hidden for user ✅");
        } else {
            System.out.println("Admin action buttons visible ❌");
        }
        driver.quit();
    }

    // TC_UI_USER_09

    @When("user clicks Categories section")
    public void user_clicks_categories_section() {
        driver.findElement(By.linkText("Categories")).click();
    }

    @Then("user should be navigated to categories page")
    public void user_should_be_navigated_to_categories_page() {
        if (driver.getCurrentUrl().contains("/categories")) {
            System.out.println("User navigated to Categories page ✅");
        }
        driver.quit();
    }

    // TC_UI_USER_10

    @When("user clicks Plants section")
    public void user_clicks_plants_section() {
        driver.findElement(By.linkText("Plants")).click();
    }

    @Then("user should be navigated to plants page")
    public void user_should_be_navigated_to_plants_page() {
        if (driver.getCurrentUrl().contains("/plants")) {
            System.out.println("User navigated to Plants page ✅");
        }
        driver.quit();
    }

    // TC_UI_USER_11

    @When("user clicks Sales section")
    public void user_clicks_sales_section() {
        driver.findElement(By.linkText("Sales")).click();
    }

    @Then("user should be navigated to sales page")
    public void user_should_be_navigated_to_sales_page() {
        if (driver.getCurrentUrl().contains("/sales")) {
            System.out.println("User navigated to Sales page ✅");
        }
        driver.quit();
    }
}
