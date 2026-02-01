package stepdefinitions.ui;

import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.SalesPage;
import pages.SellPlantPage;

public class AdminSellSteps {

    WebDriver driver;
    SalesPage salesPage;
    SellPlantPage sellPlantPage;

    @Given("admin is on the sales page")
    public void admin_is_on_the_sales_page() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Open login page
        driver.get("http://localhost:8080/ui/login");

        // Login
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Navigate to Sales page
        driver.findElement(By.cssSelector("a[href='/ui/sales']")).click();

        salesPage = new SalesPage(driver);
        Assert.assertTrue(salesPage.isOnSalesPage());
    }

    @When("admin sells {int} of {string}")
    public void admin_sells_quantity_of_plant(int quantity, String plantName) {

        // Go to Sell Plant page
        salesPage.clickSellPlant();

        sellPlantPage = new SellPlantPage(driver);
        Assert.assertTrue(sellPlantPage.isOnSellPlantPage());

        // Fill sell form
        sellPlantPage.selectPlant(plantName);
        sellPlantPage.enterQuantity(String.valueOf(quantity));
        sellPlantPage.clickSell();
    }

    @Then("sale should be successful")
    public void sale_should_be_successful() {
        Assert.assertTrue("Sale was not successful!", salesPage.isOnSalesPage());
        driver.quit();
    }
}
