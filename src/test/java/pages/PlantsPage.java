package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class PlantsPage {

    private WebDriver driver;

    public PlantsPage(WebDriver driver) {
        this.driver = driver;
    }

    // ---------- Locators (Based on your HTML) ----------
    private By pageHeading = By.tagName("h3");

    // Filters
    private By searchPlantInput = By.name("name");
    private By categorySelect   = By.name("categoryId");
    private By searchButton     = By.cssSelector("button.btn.btn-primary");
    private By resetButton      = By.cssSelector("a.btn.btn-outline-secondary");

    // Sorting headers (Name/Price/Stock have <a> in your HTML)
    private By sortNameLink  = By.cssSelector("table thead th:nth-child(1) a");
    private By sortPriceLink = By.cssSelector("table thead th:nth-child(3) a");
    private By sortStockLink = By.cssSelector("table thead th:nth-child(4) a");

    // Table
    private By tableRows = By.cssSelector("table tbody tr");

    // Pagination (your HTML snippet doesn’t show it; this is common bootstrap pagination)
    private By paginationLinks = By.cssSelector("ul.pagination a.page-link");
    private By paginationActive = By.cssSelector("ul.pagination li.active");

    // Empty state (you said: “No plants found” message)
    // If it is an alert or table row text, this is a safe generic locator:
    private By emptyStateText = By.xpath("//*[contains(text(),'No plants found') or contains(text(),'No Plants Found')]");

    // Admin actions (Add/Edit/Delete)
    // Your HTML snippet doesn’t show buttons, so we use flexible locators:
    private By addPlantButton = By.xpath("//a[contains(.,'Add') and contains(.,'Plant')]");
    private By editButtons = By.xpath("//a[contains(@href,'/ui/plants/edit') or contains(.,'Edit')]");
    private By deleteButtons = By.xpath("//*[contains(@data-bs-target,'#deleteModal') or contains(.,'Delete')]");

    // Low stock badge (you mentioned “Low” badge)
    private By lowBadge = By.xpath("//*[contains(@class,'badge') and (normalize-space()='Low' or contains(.,'Low'))]");

    // ---------- Navigation ----------
    public void open() {
        driver.get("http://localhost:8080/ui/plants");
    }

    // ---------- Validations ----------
    public boolean isOnPlantsPage() {
        return driver.getCurrentUrl().contains("/ui/plants")
                && driver.findElement(pageHeading).getText().trim().equalsIgnoreCase("Plants");
    }

    public boolean isEmptyStateShown() {
        return driver.findElements(emptyStateText).size() > 0;
    }

    public boolean isLowBadgeShown() {
        return driver.findElements(lowBadge).size() > 0;
    }

    // ---------- Filter Actions ----------
    public void enterPlantName(String name) {
        WebElement input = driver.findElement(searchPlantInput);
        input.clear();
        input.sendKeys(name);
    }

    public void selectCategory(String visibleText) {
        Select select = new Select(driver.findElement(categorySelect));
        select.selectByVisibleText(visibleText);
    }

    public void clickSearch() {
        driver.findElement(searchButton).click();
    }

    public void clickReset() {
        driver.findElement(resetButton).click();
    }

    // ---------- Sorting ----------
    public void sortByName() {
        driver.findElement(sortNameLink).click();
    }

    public void sortByPrice() {
        driver.findElement(sortPriceLink).click();
    }

    public void sortByStock() {
        driver.findElement(sortStockLink).click();
    }

    // ---------- Table helpers ----------
    public boolean isPlantDisplayed(String plantName) {
        for (WebElement row : driver.findElements(tableRows)) {
            List<WebElement> cols = row.findElements(By.cssSelector("td"));
            if (cols.size() >= 1 && cols.get(0).getText().trim().equalsIgnoreCase(plantName)) {
                return true;
            }
        }
        return false;
    }

    public int getRowCount() {
        return driver.findElements(tableRows).size();
    }

    // ---------- Pagination ----------
    public boolean isPaginationVisible() {
        return driver.findElements(paginationLinks).size() > 0;
    }

    public void clickPaginationByText(String pageText) {
        for (WebElement link : driver.findElements(paginationLinks)) {
            if (link.getText().trim().equals(pageText)) {
                link.click();
                return;
            }
        }
        throw new NoSuchElementException("Pagination link not found: " + pageText);
    }

    // ---------- Role-based visibility ----------
    public boolean isAddPlantVisible() {
        return driver.findElements(addPlantButton).size() > 0;
    }

    public boolean isAnyEditVisible() {
        return driver.findElements(editButtons).size() > 0;
    }

    public boolean isAnyDeleteVisible() {
        return driver.findElements(deleteButtons).size() > 0;
    }
}
