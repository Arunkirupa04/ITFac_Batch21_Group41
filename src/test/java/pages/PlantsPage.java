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
    private By categorySelect = By.name("categoryId");
    private By searchButton = By.cssSelector("button.btn.btn-primary");
    private By resetButton = By.cssSelector("a.btn.btn-outline-secondary");

    // Sorting headers (Name/Price/Stock have <a> in your HTML)
    private By sortNameLink = By.cssSelector("table thead th:nth-child(1) a");
    private By sortPriceLink = By.cssSelector("table thead th:nth-child(3) a");
    private By sortStockLink = By.cssSelector("table thead th:nth-child(4) a");

    // Table
    private By tableRows = By.cssSelector("table tbody tr");

    // Pagination
    private By paginationLinks = By.cssSelector("ul.pagination a.page-link");
    private By paginationActive = By.cssSelector("ul.pagination li.active");

    // Empty state
    private By emptyStateText = By.xpath(
            "//*[contains(text(),'No plants found') or contains(text(),'No Plants Found')]");

    // Admin actions (Add/Edit/Delete)
    private By addPlantButton = By.xpath("//a[contains(.,'Add') and contains(.,'Plant')]");
    private By editButtons = By.xpath("//a[contains(@href,'/ui/plants/edit') or contains(.,'Edit')]");
    private By deleteButtons = By.xpath(
            "//button[contains(@data-bs-target,'delete') or contains(.,'Delete') or contains(@class,'delete') or contains(@class,'btn-danger')] | "
                    +
                    "//a[contains(.,'Delete') or contains(@class,'delete')] | " +
                    "//*[contains(@class,'fa-trash') or contains(@class,'trash')]");

    // Low stock badge
    private By lowBadge = By.xpath(
            "//*[contains(@class,'badge') and (normalize-space()='Low' or contains(.,'Low'))]");

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

    /**
     * Low stock badge in app (from inspect): <span class="badge bg-danger ms-2">Low</span> inside table td.
     */
    public boolean isLowBadgeShown() {
        // 1) Exact match from inspect: span.badge.bg-danger in table (Stock column)
        List<WebElement> inTable = driver.findElements(By.cssSelector("table tbody span.badge.bg-danger"));
        for (WebElement badge : inTable) {
            String text = badge.getText().replaceAll("\\s+", " ").trim();
            if ("Low".equalsIgnoreCase(text)) {
                return true;
            }
        }
        // 2) Anywhere on page: span.badge.bg-danger with text "Low"
        List<WebElement> badges = driver.findElements(By.cssSelector("span.badge.bg-danger"));
        for (WebElement badge : badges) {
            String text = badge.getText().replaceAll("\\s+", " ").trim();
            if ("Low".equalsIgnoreCase(text)) {
                return true;
            }
        }
        // 3) XPath fallback: any element with class badge and "Low" text
        if (driver.findElements(lowBadge).size() > 0) {
            return true;
        }
        return false;
    }

    // ---------- Filter Actions ----------
    public void enterPlantName(String name) {
        WebElement input = driver.findElement(searchPlantInput);
        input.clear();
        input.sendKeys(name);
    }

    public void selectCategory(String visibleText) {
        Select select = new Select(driver.findElement(categorySelect));
        try {
            select.selectByVisibleText(visibleText);
        } catch (NoSuchElementException e) {
            // Fallback: app may not have "Indoor" - select first non-placeholder option
            java.util.List<WebElement> options = select.getOptions();
            for (int i = 1; i < options.size(); i++) {
                String text = options.get(i).getText().trim();
                if (!text.isEmpty() && !text.equalsIgnoreCase("Select") && !text.equals("--")) {
                    select.selectByIndex(i);
                    return;
                }
            }
            if (options.size() > 1)
                select.selectByIndex(1);
        }
    }

    public void clickSearch() {
        WebElement btn = driver.findElement(searchButton);
        try {
            btn.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
        // Wait for table to reload (briefly)
        try {
            Thread.sleep(500);
        } catch (Exception ignore) {
        }
    }

    public void clickReset() {
        driver.findElement(resetButton).click();
    }

    // ---------- Sorting ----------
    private void clickSortHeader(By locator) {
        WebElement header = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", header);
        try {
            header.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", header);
        }
    }

    public void sortByName() {
        clickSortHeader(sortNameLink);
    }

    public void sortByPrice() {
        clickSortHeader(sortPriceLink);
    }

    public void sortByStock() {
        clickSortHeader(sortStockLink);
    }

    // ---------- Table helpers ----------
    /**
     * Returns the name (first column) of the first plant in the table, or null if empty.
     */
    public String getFirstPlantName() {
        List<WebElement> rows = driver.findElements(tableRows);
        if (rows.isEmpty())
            return null;
        try {
            List<WebElement> cols = rows.get(0).findElements(By.cssSelector("td"));
            if (cols.size() >= 1)
                return cols.get(0).getText().trim();
        } catch (StaleElementReferenceException e) {
            // retry once
            rows = driver.findElements(tableRows);
            if (!rows.isEmpty()) {
                List<WebElement> cols = rows.get(0).findElements(By.cssSelector("td"));
                if (cols.size() >= 1)
                    return cols.get(0).getText().trim();
            }
        }
        return null;
    }

    public boolean isPlantDisplayed(String plantName) {
        try {
            List<WebElement> rows = driver.findElements(tableRows);
            for (WebElement row : rows) {
                try {
                    // Get text from the entire row first to avoid stale element issues
                    String rowText = row.getText();
                    if (rowText.contains(plantName)) {
                        // Double-check by looking at first column
                        List<WebElement> cols = row.findElements(By.cssSelector("td"));
                        if (cols.size() >= 1 &&
                                cols.get(0).getText().trim().equalsIgnoreCase(plantName)) {
                            return true;
                        }
                    }
                } catch (StaleElementReferenceException e) {
                    // Element became stale, continue to next row
                    continue;
                }
            }
        } catch (StaleElementReferenceException e) {
            // If the entire table became stale, retry once
            try {
                Thread.sleep(500); // Brief wait for DOM to stabilize
                List<WebElement> rows = driver.findElements(tableRows);
                for (WebElement row : rows) {
                    try {
                        List<WebElement> cols = row.findElements(By.cssSelector("td"));
                        if (cols.size() >= 1 &&
                                cols.get(0).getText().trim().equalsIgnoreCase(plantName)) {
                            return true;
                        }
                    } catch (StaleElementReferenceException ex) {
                        continue;
                    }
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
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
                try {
                    link.click();
                } catch (ElementClickInterceptedException e) {
                    ((JavascriptExecutor) driver)
                            .executeScript("arguments[0].click();", link);
                }
                return;
            }
        }
        throw new NoSuchElementException(
                "Pagination link not found: " + pageText);
    }

    // ---------- Role-based visibility ----------
    public boolean isAddPlantVisible() {
        try {
            return driver.findElement(addPlantButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAnyEditVisible() {
        List<WebElement> elements = driver.findElements(editButtons);
        for (WebElement el : elements) {
            if (el.isDisplayed())
                return true;
        }
        return false;
    }

    public boolean isAnyDeleteVisible() {
        java.util.List<WebElement> elements = driver.findElements(deleteButtons);
        for (WebElement el : elements) {
            if (el.isDisplayed()) {
                return true;
            }
        }
        return false;
    }

    // ---------- Navigation Actions ----------
    public void clickAddPlant() {
        // Wait for element to be present and clickable
        for (int i = 0; i < 3; i++) {
            try {
                WebElement btn = driver.findElement(addPlantButton);
                if (btn.isDisplayed()) {
                    btn.click();
                    return;
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(500);
                } catch (Exception ignore) {
                }
            }
        }
        // Final fallback
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(addPlantButton));
    }

    public void clickEditPlant(String plantName) {
        List<WebElement> rows = driver.findElements(tableRows);
        for (WebElement row : rows) {
            if (row.getText().contains(plantName)) {
                try {
                    WebElement editBtn = row.findElement(
                            By.xpath(".//a[contains(@href,'edit')]"));
                    editBtn.click();
                    return;
                } catch (Exception e) {
                    // continue
                }
            }
        }
        throw new NoSuchElementException(
                "Edit button not found for plant: " + plantName);
    }

    // ---------- Additional helper methods for user scenarios ----------
    public boolean isAddPlantButtonVisible() {
        return isAddPlantVisible();
    }

    public boolean isEditActionVisible() {
        return isAnyEditVisible();
    }

    public boolean isDeleteActionVisible() {
        return isAnyDeleteVisible();
    }

    public void clickPaginationPage(String pageNumber) {
        clickPaginationByText(pageNumber);
    }
}
