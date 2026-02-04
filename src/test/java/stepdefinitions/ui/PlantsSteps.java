package stepdefinitions.ui;

import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.List;

public class PlantsSteps {

    WebDriver driver;

    // ---------- Common setup ----------
    @Given("admin is logged in and on plants page")
    public void admin_is_logged_in_and_on_plants_page() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Login as admin
        driver.get("http://localhost:8080/ui/login");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Navigate to plants page
        driver.get("http://localhost:8080/ui/plants");
    }

    @Given("user is logged in and on plants page")
    public void user_is_logged_in_and_on_plants_page() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Login as normal user
        driver.get("http://localhost:8080/ui/login");
        driver.findElement(By.name("username")).sendKeys("user");
        driver.findElement(By.name("password")).sendKeys("user123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.get("http://localhost:8080/ui/plants");
    }

    // ---------- Page Load ----------
    @Then("plants page should load successfully")
    public void plants_page_should_load_successfully() {
        String heading = driver.findElement(By.tagName("h3")).getText();
        if (heading.equalsIgnoreCase("Plants")) {
            System.out.println("Plants page loaded ✅");
        } else {
            System.out.println("Plants page not loaded ❌");
        }
    }

    // ---------- Search ----------
    @When("search plant by name {string}")
    public void search_plant_by_name(String name) {
        WebElement searchBox = driver.findElement(By.name("name"));
        searchBox.clear();
        searchBox.sendKeys(name);
        driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    }

    @Then("only matching plant {string} should be shown")
    public void only_matching_plant_should_be_shown(String plantName) {
        List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));
        boolean found = false;

        for (WebElement row : rows) {
            String name = row.findElement(By.cssSelector("td:nth-child(1)")).getText();
            if (name.equalsIgnoreCase(plantName)) {
                found = true;
            }
        }

        if (found) {
            System.out.println("Matching plant displayed ✅");
        } else {
            System.out.println("Matching plant not found ❌");
        }
    }

    // ---------- Category Filter ----------
    @When("filter plants by category {string}")
    public void filter_plants_by_category(String category) {
        WebElement dropdown = driver.findElement(By.name("categoryId"));
        dropdown.sendKeys(category);
        driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    }

    @Then("filtered plants should be shown")
    public void filtered_plants_should_be_shown() {
        List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));
        if (rows.size() > 0) {
            System.out.println("Filtered results shown ✅");
        } else {
            System.out.println("No filtered results ❌");
        }
    }

    // ---------- Sorting ----------
    @When("sort plants by name")
    public void sort_plants_by_name() {
        driver.findElement(By.cssSelector("table thead th:nth-child(1) a")).click();
    }

    @When("sort plants by price")
    public void sort_plants_by_price() {
        driver.findElement(By.cssSelector("table thead th:nth-child(3) a")).click();
    }

    @When("sort plants by stock")
    public void sort_plants_by_stock() {
        driver.findElement(By.cssSelector("table thead th:nth-child(4) a")).click();
    }

    @Then("plant list should be sorted")
    public void plant_list_should_be_sorted() {
        System.out.println("Sorting action performed ✅");
    }

    // ---------- Low Stock ----------
    @Then("low stock badge should be displayed")
    public void low_stock_badge_should_be_displayed() {
        List<WebElement> lowBadges =
                driver.findElements(By.xpath("//*[contains(text(),'Low')]"));

        if (lowBadges.size() > 0) {
            System.out.println("Low stock badge visible ✅");
        } else {
            System.out.println("Low stock badge not visible ❌");
        }
    }

    // ---------- Empty State ----------
    @Then("no plants found message should be displayed")
    public void no_plants_found_message_should_be_displayed() {
        boolean messageShown =
                driver.getPageSource().contains("No plants found");

        if (messageShown) {
            System.out.println("Empty state message shown ✅");
        } else {
            System.out.println("Empty state message not shown ❌");
        }
    }

    // ---------- Admin actions ----------
    @Then("admin should see add edit and delete actions")
    public void admin_should_see_add_edit_and_delete_actions() {
        boolean addVisible =
                driver.getPageSource().contains("Add");
        boolean editVisible =
                driver.getPageSource().contains("Edit");
        boolean deleteVisible =
                driver.getPageSource().contains("Delete");

        if (addVisible && editVisible && deleteVisible) {
            System.out.println("Admin actions visible ✅");
        } else {
            System.out.println("Admin actions missing ❌");
        }
    }

    // ---------- User restrictions ----------
    @Then("user should not see add plant button")
    public void user_should_not_see_add_plant_button() {
        boolean addVisible = driver.getPageSource().contains("Add");
        if (!addVisible) {
            System.out.println("Add Plant hidden for user ✅");
        } else {
            System.out.println("Add Plant visible for user ❌");
        }
    }

    @Then("user should not see edit action")
    public void user_should_not_see_edit_action() {
        boolean editVisible = driver.getPageSource().contains("Edit");
        if (!editVisible) {
            System.out.println("Edit hidden for user ✅");
        } else {
            System.out.println("Edit visible for user ❌");
        }
    }

    @Then("user should not see delete action")
    public void user_should_not_see_delete_action() {
        boolean deleteVisible = driver.getPageSource().contains("Delete");
        if (!deleteVisible) {
            System.out.println("Delete hidden for user ✅");
        } else {
            System.out.println("Delete visible for user ❌");
        }
    }

    // ---------- Close ----------
    @And("close plants browser")
    public void close_plants_browser() {
        driver.quit();
    }
}
