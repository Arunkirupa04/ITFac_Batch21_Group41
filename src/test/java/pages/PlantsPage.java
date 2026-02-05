//package pages;
//
//import org.openqa.selenium.*;
//import org.openqa.selenium.support.ui.Select;
//import java.util.List;
//
//public class PlantsPage {
//
//    private WebDriver driver;
//
//    public PlantsPage(WebDriver driver) {
//        this.driver = driver;
//    }
//
//    // ---------- Locators (Based on your HTML) ----------
//    private By pageHeading = By.tagName("h3");
//
//    // Filters
//    private By searchPlantInput = By.name("name");
//    private By categorySelect = By.name("categoryId");
//    private By searchButton = By.cssSelector("button.btn.btn-primary");
//    private By resetButton = By.cssSelector("a.btn.btn-outline-secondary");
//
//    // Sorting headers (Name/Price/Stock have <a> in your HTML)
//    private By sortNameLink = By.cssSelector("table thead th:nth-child(1) a");
//    private By sortPriceLink = By.cssSelector("table thead th:nth-child(3) a");
//    private By sortStockLink = By.cssSelector("table thead th:nth-child(4) a");
//
//    // Table
//    private By tableRows = By.cssSelector("table tbody tr");
//
//    // Pagination
//    private By paginationLinks = By.cssSelector("ul.pagination a.page-link");
//    private By paginationActive = By.cssSelector("ul.pagination li.active");
//
//    // Empty state
//    private By emptyStateText = By.xpath(
//            "//*[contains(text(),'No plants found') or contains(text(),'No Plants Found')]");
//
//    // Admin actions (Add/Edit/Delete)
//    private By addPlantButton = By.xpath("//a[contains(.,'Add') and contains(.,'Plant')]");
//    private By editButtons = By.xpath("//a[contains(@href,'/ui/plants/edit') or contains(.,'Edit')]");
//    private By deleteButtons = By.xpath(
//            "//*[contains(@data-bs-target,'#deleteModal') or contains(.,'Delete')]");
//
//    // Low stock badge
//    private By lowBadge = By.xpath(
//            "//*[contains(@class,'badge') and (normalize-space()='Low' or contains(.,'Low'))]");
//
//    // ---------- Navigation ----------
//    public void open() {
//        driver.get("http://localhost:8080/ui/plants");
//    }
//
//    // ---------- Validations ----------
//    public boolean isOnPlantsPage() {
//        return driver.getCurrentUrl().contains("/ui/plants")
//                && driver.findElement(pageHeading).getText().trim().equalsIgnoreCase("Plants");
//    }
//
//    public boolean isEmptyStateShown() {
//        return driver.findElements(emptyStateText).size() > 0;
//    }
//
//    // public boolean isLowBadgeShown() {
//    // return driver.findElements(lowBadge).size() > 0;
//    // }
//    public boolean isLowBadgeShown() {
//        List<WebElement> badges = driver.findElements(By.cssSelector("span.badge.bg-danger"));
//
//        for (WebElement badge : badges) {
//            if (badge.getText().trim().equalsIgnoreCase("Low")) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    // ---------- Filter Actions ----------
//    public void enterPlantName(String name) {
//        WebElement input = driver.findElement(searchPlantInput);
//        input.clear();
//        input.sendKeys(name);
//    }
//
//    public void selectCategory(String visibleText) {
//        Select select = new Select(driver.findElement(categorySelect));
//        select.selectByVisibleText(visibleText);
//    }
//
//    public void clickSearch() {
//        driver.findElement(searchButton).click();
//    }
//
//    public void clickReset() {
//        driver.findElement(resetButton).click();
//    }
//
//    // ---------- Sorting ----------
//    public void sortByName() {
//        driver.findElement(sortNameLink).click();
//    }
//
//    public void sortByPrice() {
//        driver.findElement(sortPriceLink).click();
//    }
//
//    public void sortByStock() {
//        driver.findElement(sortStockLink).click();
//    }
//
//    // ---------- Table helpers ----------
//    public boolean isPlantDisplayed(String plantName) {
//        try {
//            List<WebElement> rows = driver.findElements(tableRows);
//            for (WebElement row : rows) {
//                try {
//                    // Get text from the entire row first to avoid stale element issues
//                    String rowText = row.getText();
//                    if (rowText.contains(plantName)) {
//                        // Double-check by looking at first column
//                        List<WebElement> cols = row.findElements(By.cssSelector("td"));
//                        if (cols.size() >= 1 &&
//                                cols.get(0).getText().trim().equalsIgnoreCase(plantName)) {
//                            return true;
//                        }
//                    }
//                } catch (StaleElementReferenceException e) {
//                    // Element became stale, continue to next row
//                    continue;
//                }
//            }
//        } catch (StaleElementReferenceException e) {
//            // If the entire table became stale, retry once
//            try {
//                Thread.sleep(500); // Brief wait for DOM to stabilize
//                List<WebElement> rows = driver.findElements(tableRows);
//                for (WebElement row : rows) {
//                    try {
//                        List<WebElement> cols = row.findElements(By.cssSelector("td"));
//                        if (cols.size() >= 1 &&
//                                cols.get(0).getText().trim().equalsIgnoreCase(plantName)) {
//                            return true;
//                        }
//                    } catch (StaleElementReferenceException ex) {
//                        continue;
//                    }
//                }
//            } catch (InterruptedException ie) {
//                Thread.currentThread().interrupt();
//            }
//        }
//        return false;
//    }
//
//    public int getRowCount() {
//        return driver.findElements(tableRows).size();
//    }
//
//    // ---------- Pagination ----------
//    public boolean isPaginationVisible() {
//        return driver.findElements(paginationLinks).size() > 0;
//    }
//
//    public void clickPaginationByText(String pageText) {
//        for (WebElement link : driver.findElements(paginationLinks)) {
//            if (link.getText().trim().equals(pageText)) {
//                try {
//                    link.click();
//                } catch (ElementClickInterceptedException e) {
//                    ((JavascriptExecutor) driver)
//                            .executeScript("arguments[0].click();", link);
//                }
//                return;
//            }
//        }
//        throw new NoSuchElementException(
//                "Pagination link not found: " + pageText);
//    }
//
//    // ---------- Role-based visibility ----------
//    public boolean isAddPlantVisible() {
//        return driver.findElements(addPlantButton).size() > 0;
//    }
//
//    public boolean isAnyEditVisible() {
//        return driver.findElements(editButtons).size() > 0;
//    }
//
//    public boolean isAnyDeleteVisible() {
//        return driver.findElements(deleteButtons).size() > 0;
//    }
//
//    // ---------- Navigation Actions ----------
//    public void clickAddPlant() {
//        driver.findElement(addPlantButton).click();
//    }
//
//    public void clickEditPlant(String plantName) {
//        List<WebElement> rows = driver.findElements(tableRows);
//        for (WebElement row : rows) {
//            if (row.getText().contains(plantName)) {
//                try {
//                    WebElement editBtn = row.findElement(
//                            By.xpath(".//a[contains(@href,'edit')]"));
//                    editBtn.click();
//                    return;
//                } catch (Exception e) {
//                    // continue
//                }
//            }
//        }
//        throw new NoSuchElementException(
//                "Edit button not found for plant: " + plantName);
//    }
//}

package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class PlantsPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public PlantsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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
            "//*[contains(text(),'No plants found') or contains(text(),'No Plants Found')]"
    );

    // Admin actions (Add/Edit/Delete)
    // NOTE: This original locator might fail if UI uses icon-only button, different text, etc.
    private By addPlantButton = By.xpath("//a[contains(.,'Add') and contains(.,'Plant')]");
    private By editButtons = By.xpath("//a[contains(@href,'/ui/plants/edit') or contains(.,'Edit')]");
    private By deleteButtons = By.xpath("//*[contains(@data-bs-target,'#deleteModal') or contains(.,'Delete')]");

    // Low stock badge
    private By lowBadge = By.xpath(
            "//*[contains(@class,'badge') and (normalize-space()='Low' or contains(.,'Low'))]"
    );

    // ---------- Navigation ----------
    public void open() {
        driver.get("http://localhost:8080/ui/plants");
    }

    // ---------- Validations ----------
    public boolean isOnPlantsPage() {
        try {
            boolean urlOk = driver.getCurrentUrl().contains("/ui/plants");
            WebElement h = driver.findElement(pageHeading);
            String heading = h.getText().trim();
            return urlOk && heading.equalsIgnoreCase("Plants");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEmptyStateShown() {
        return driver.findElements(emptyStateText).size() > 0;
    }

    public boolean isLowBadgeShown() {
        // more stable: check exact bootstrap badge class used in your UI
        List<WebElement> badges = driver.findElements(By.cssSelector("span.badge.bg-danger"));
        for (WebElement badge : badges) {
            if (badge.getText().trim().equalsIgnoreCase("Low")) {
                return true;
            }
        }
        return false;
    }

    // ---------- Filter Actions ----------
    public void enterPlantName(String name) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchPlantInput));
        input.clear();
        input.sendKeys(name);
    }

    public void selectCategory(String visibleText) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(categorySelect));
        Select select = new Select(el);

        // If empty string passed, try to select first option (often placeholder)
        if (visibleText == null || visibleText.trim().isEmpty()) {
            try {
                select.selectByIndex(0);
            } catch (Exception ignored) {}
            return;
        }

        select.selectByVisibleText(visibleText);
    }

    public void clickSearch() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        safeClick(btn);
    }

    public void clickReset() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(resetButton));
        safeClick(btn);
    }

    // ---------- Sorting ----------
    public void sortByName() {
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(sortNameLink));
        safeClick(link);
    }

    public void sortByPrice() {
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(sortPriceLink));
        safeClick(link);
    }

    public void sortByStock() {
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(sortStockLink));
        safeClick(link);
    }

    // ---------- Table helpers ----------
    public boolean isPlantDisplayed(String plantName) {
        try {
            List<WebElement> rows = driver.findElements(tableRows);
            for (WebElement row : rows) {
                try {
                    String rowText = row.getText();
                    if (rowText.contains(plantName)) {
                        List<WebElement> cols = row.findElements(By.cssSelector("td"));
                        if (cols.size() >= 1 &&
                                cols.get(0).getText().trim().equalsIgnoreCase(plantName)) {
                            return true;
                        }
                    }
                } catch (StaleElementReferenceException e) {
                    continue;
                }
            }
        } catch (StaleElementReferenceException e) {
            // retry once
            try {
                Thread.sleep(400);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            List<WebElement> rows = driver.findElements(tableRows);
            for (WebElement row : rows) {
                try {
                    List<WebElement> cols = row.findElements(By.cssSelector("td"));
                    if (cols.size() >= 1 &&
                            cols.get(0).getText().trim().equalsIgnoreCase(plantName)) {
                        return true;
                    }
                } catch (StaleElementReferenceException ignored) {}
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
        List<WebElement> links = driver.findElements(paginationLinks);
        for (WebElement link : links) {
            if (link.getText().trim().equals(pageText)) {
                safeClick(link);
                return;
            }
        }
        throw new NoSuchElementException("Pagination link not found: " + pageText);
    }

    // ---------- Role-based visibility ----------
    public boolean isAddPlantVisible() {
        // Try original first, then robust find
        if (driver.findElements(addPlantButton).size() > 0) return true;
        return findAddPlantElement() != null;
    }

    public boolean isAnyEditVisible() {
        return driver.findElements(editButtons).size() > 0;
    }

    public boolean isAnyDeleteVisible() {
        return driver.findElements(deleteButtons).size() > 0;
    }

    // ---------- Navigation Actions ----------
    public void clickAddPlant() {
        // 1) Try find by multiple locators and click
        WebElement add = findAddPlantElement();
        if (add != null) {
            safeClick(add);
            return;
        }

        // 2) Fallback: direct URLs
        String base = "http://localhost:8080";
        String[] possible = new String[] {
                base + "/ui/plants/add",
                base + "/ui/plants/new",
                base + "/ui/plants/create",
                base + "/ui/plants/add-plant",
                base + "/ui/plants/addPlant"
        };

        for (String url : possible) {
            try {
                driver.get(url);

                String u = driver.getCurrentUrl();
                if (u.contains("/ui/plants") && (u.contains("add") || u.contains("new") || u.contains("create"))) {
                    return;
                }

                // Heuristic: if Save/Add/Create button exists, accept page
                if (driver.findElements(By.xpath(
                        "//button[contains(.,'Save') or contains(.,'Add') or contains(.,'Create') or contains(.,'Update')]"
                )).size() > 0) {
                    return;
                }
            } catch (Exception ignored) {}
        }

        throw new NoSuchElementException(
                "Add Plant button/link not found. Please inspect actual UI element text/href."
        );
    }

    private WebElement findAddPlantElement() {
        By[] candidates = new By[] {

                // Original locator
                addPlantButton,

                // href patterns
                By.cssSelector("a[href*='/ui/plants/add'], a[href*='/ui/plants/new'], a[href*='/ui/plants/create']"),
                By.cssSelector("a[href*='/plants/add'], a[href*='/plants/new'], a[href*='/plants/create']"),

                // Text-based for Add/New/Create
                By.xpath("//*[self::a or self::button][contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'add')]"),
                By.xpath("//*[self::a or self::button][contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'new')]"),
                By.xpath("//*[self::a or self::button][contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'create')]"),

                // Plant + (Add/New/Create)
                By.xpath("//*[self::a or self::button][contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'plant') and " +
                        "(contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'add') or " +
                        " contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'new') or " +
                        " contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'create'))]"),

                // Icon-only (+) buttons (Bootstrap Icons / FontAwesome)
                By.cssSelector("a i.bi-plus, button i.bi-plus"),
                By.cssSelector("a i.fa-plus, button i.fa-plus"),

                // Common primary/success buttons (some UIs use green/blue button without Add text)
                By.cssSelector("a.btn.btn-success, button.btn.btn-success, a.btn.btn-primary, button.btn.btn-primary"),

                // plus sign visible
                By.xpath("//*[self::a or self::button][contains(normalize-space(.),'+')]")
        };

        for (By by : candidates) {
            try {
                WebElement el = wait.until(ExpectedConditions.elementToBeClickable(by));
                if (el != null) return el;
            } catch (Exception ignored) {}
        }
        return null;
    }

    public void clickEditPlant(String plantName) {
        List<WebElement> rows = driver.findElements(tableRows);
        for (WebElement row : rows) {
            if (row.getText().contains(plantName)) {
                try {
                    WebElement editBtn = row.findElement(By.xpath(".//a[contains(@href,'edit')]"));
                    safeClick(editBtn);
                    return;
                } catch (Exception ignored) {}
            }
        }
        throw new NoSuchElementException("Edit button not found for plant: " + plantName);
    }

    // ---------- Utility ----------
    private void safeClick(WebElement element) {
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        } catch (StaleElementReferenceException e) {
            // If stale, try JS click anyway
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }
}