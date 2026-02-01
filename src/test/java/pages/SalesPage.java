package pages;

import org.openqa.selenium.*;

import java.util.List;

public class SalesPage {

    WebDriver driver;

    public SalesPage(WebDriver driver) {
        this.driver = driver;
    }

    // ---------- Locators ----------
    private By pageHeading = By.tagName("h3");
    private By sellPlantButton = By.linkText("Sell Plant");
    private By salesTableRows = By.cssSelector("table tbody tr");
    private By nextPageBtn = By.linkText("Next");
    private By previousPageBtn = By.linkText("Previous");

    // ---------- Page validations ----------
    public boolean isOnSalesPage() {
        return driver.getCurrentUrl().endsWith("/ui/sales")
                && driver.findElement(pageHeading).getText().equals("Sales");
    }

    // ---------- Actions ----------
    public void clickSellPlant() {
        driver.findElement(sellPlantButton).click();
    }

    // ---------- Table ----------
    public int getSalesCount() {
        List<WebElement> rows = driver.findElements(salesTableRows);
        return rows.size();
    }

    public boolean isSalesTableDisplayed() {
        return driver.findElements(salesTableRows).size() > 0;
    }

    // ---------- Pagination ----------
    public void clickNextPage() {
        driver.findElement(nextPageBtn).click();
    }

    public void clickPreviousPage() {
        driver.findElement(previousPageBtn).click();
    }
}
