package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class CategoriesPage {

    WebDriver driver;

    public CategoriesPage(WebDriver driver) {
        this.driver = driver;
    }

    // ---------- Locators ----------
    private By pageHeading = By.xpath("//h3[contains(text(),'Categories')]");
    private By searchInput = By.name("name");
    private By parentDropdown = By.name("parentId");
    private By searchButton = By.xpath("//button[contains(text(),'Search')]");
    private By resetButton = By.linkText("Reset");
    private By addCategoryButton = By.linkText("Add A Category");
    private By categoryTable = By.cssSelector("table.table-striped");
    private By tableRows = By.cssSelector("table tbody tr");
    private By idColumnHeader = By.xpath("//th//a[contains(@href,'sortField=id')]");
    private By nameColumnHeader = By.xpath("//th//a[contains(@href,'sortField=name')]");
    private By parentColumnHeader = By.xpath("//th//a[contains(@href,'sortField=parent')]");
    private By editButtons = By.cssSelector("a.btn-outline-primary, a[href*='/edit/']");
    private By deleteButtons = By
            .cssSelector("button.btn-outline-danger, a[href*='/delete/'], button[contains(text(),'Delete')]");
    private By sortIndicator = By.xpath("//span[text()=' ↑' or text()=' ↓']");
    private By paginationLinks = By.cssSelector(".pagination a");
    private By emptyMessage = By.xpath("//td[contains(text(),'No category found')]");

    // ---------- Page Validations ----------
    public boolean isOnCategoriesPage() {
        try {
            return driver.getCurrentUrl().contains("/ui/categories")
                    && driver.findElement(pageHeading).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isSearchInputVisible() {
        return driver.findElement(searchInput).isDisplayed();
    }

    public boolean isParentDropdownVisible() {
        return driver.findElement(parentDropdown).isDisplayed();
    }

    public boolean isSearchButtonVisible() {
        return driver.findElement(searchButton).isDisplayed();
    }

    public boolean isResetButtonVisible() {
        return driver.findElement(resetButton).isDisplayed();
    }

    public boolean isAddCategoryButtonVisible() {
        try {
            return driver.findElement(addCategoryButton).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isTableDisplayed() {
        return driver.findElement(categoryTable).isDisplayed();
    }

    public boolean areSearchFilterResetAddButtonsVisible() {
        return isSearchButtonVisible() && isResetButtonVisible() && isAddCategoryButtonVisible();
    }

    // ---------- Actions ----------
    public void navigateToCategoriesPage() {
        driver.get("http://localhost:8080/ui/categories");
    }

    public void enterSearchKeyword(String keyword) {
        WebElement search = driver.findElement(searchInput);
        search.clear();
        search.sendKeys(keyword);
    }

    public void selectParentCategory(String parentName) {
        Select select = new Select(driver.findElement(parentDropdown));
        try {
            select.selectByVisibleText(parentName);
        } catch (NoSuchElementException e) {
            // Parent may not exist when run as user (e.g. Cat Test01 not created); use first available option
            java.util.List<WebElement> options = select.getOptions();
            for (int i = 0; i < options.size(); i++) {
                String text = options.get(i).getText().trim();
                if (!text.isEmpty() && !text.equalsIgnoreCase("all") && !text.equals("-")) {
                    select.selectByIndex(i);
                    return;
                }
            }
            if (options.size() > 1) {
                select.selectByIndex(1);
            }
            throw e;
        }
    }

    public void clickSearchButton() {
        driver.findElement(searchButton).click();
    }

    public void clickResetButton() {
        driver.findElement(resetButton).click();
    }

    public void clickAddCategoryButton() {
        driver.findElement(addCategoryButton).click();
    }

    public void clickIdColumnHeader() {
        driver.findElement(idColumnHeader).click();
    }

    public void clickNameColumnHeader() {
        driver.findElement(nameColumnHeader).click();
    }

    public void clickParentColumnHeader() {
        try {
            driver.findElement(parentColumnHeader).click();
        } catch (NoSuchElementException e) {
            System.out.println("⚠️ Parent column is not sortable");
        }
    }

    public void clickEditForCategory(int index) {
        List<WebElement> editButtonsList = driver.findElements(editButtons);
        if (index < editButtonsList.size()) {
            editButtonsList.get(index).click();
        }
    }

    public void clickEditForCategoryById(String categoryId) {
        driver.findElement(By.cssSelector("a[href='/ui/categories/edit/" + categoryId + "']")).click();
    }

    public void clickPaginationPage(int pageNumber) {
        WebElement pageLink = driver.findElement(By.linkText(String.valueOf(pageNumber)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", pageLink);
        try {
            pageLink.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", pageLink);
        }
    }

    // ---------- Table Operations ----------
    public int getCategoryCount() {
        List<WebElement> rows = driver.findElements(tableRows);
        // Filter out empty state rows
        return (int) rows.stream()
                .filter(row -> !row.getText().contains("No category found"))
                .count();
    }

    public boolean isTableEmpty() {
        try {
            return driver.findElement(emptyMessage).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public String getEmptyStateMessage() {
        try {
            return driver.findElement(emptyMessage).getText();
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    public String getSearchInputValue() {
        return driver.findElement(searchInput).getAttribute("value");
    }

    public List<Integer> getCategoryIds() {
        List<Integer> ids = new ArrayList<>();
        List<WebElement> rows = driver.findElements(tableRows);
        for (WebElement row : rows) {
            if (!row.getText().contains("No category found")) {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                if (!cells.isEmpty()) {
                    ids.add(Integer.parseInt(cells.get(0).getText()));
                }
            }
        }
        return ids;
    }

    public List<String> getCategoryNames() {
        List<String> names = new ArrayList<>();
        List<WebElement> rows = driver.findElements(tableRows);
        for (WebElement row : rows) {
            if (!row.getText().contains("No category found")) {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                if (cells.size() >= 2) {
                    names.add(cells.get(1).getText());
                }
            }
        }
        return names;
    }

    public List<String> getParentNames() {
        List<String> parents = new ArrayList<>();
        List<WebElement> rows = driver.findElements(tableRows);
        for (WebElement row : rows) {
            if (!row.getText().contains("No category found")) {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                if (cells.size() >= 3) {
                    parents.add(cells.get(2).getText().trim());
                }
            }
        }
        return parents;
    }

    public boolean isSortedById(String direction) {
        List<Integer> ids = getCategoryIds();
        if (ids.size() <= 1)
            return true;

        for (int i = 0; i < ids.size() - 1; i++) {
            if (direction.equalsIgnoreCase("ascending")) {
                if (ids.get(i) > ids.get(i + 1))
                    return false;
            } else if (direction.equalsIgnoreCase("descending")) {
                if (ids.get(i) < ids.get(i + 1))
                    return false;
            }
        }
        return true;
    }

    public boolean isSortedByName(String direction) {
        List<String> names = getCategoryNames();
        if (names.size() <= 1)
            return true;

        for (int i = 0; i < names.size() - 1; i++) {
            int comparison = names.get(i).compareToIgnoreCase(names.get(i + 1));
            if (direction.equalsIgnoreCase("ascending")) {
                if (comparison > 0)
                    return false;
            } else if (direction.equalsIgnoreCase("descending")) {
                if (comparison < 0)
                    return false;
            }
        }
        return true;
    }

    public boolean isSortedByParent(String direction) {
        List<String> parents = getParentNames();
        if (parents.size() <= 1)
            return true;

        for (int i = 0; i < parents.size() - 1; i++) {
            int comparison = parents.get(i).compareToIgnoreCase(parents.get(i + 1));
            if (direction.equalsIgnoreCase("ascending")) {
                if (comparison > 0)
                    return false;
            } else if (direction.equalsIgnoreCase("descending")) {
                if (comparison < 0)
                    return false;
            }
        }
        return true;
    }

    public boolean isPaginationVisible() {
        try {
            return !driver.findElements(paginationLinks).isEmpty();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean categoryExistsInTable(String categoryName) {
        List<String> names = getCategoryNames();
        return names.contains(categoryName);
    }

    // ---------- Permission/Visibility Checks for User Role ----------
    public List<WebElement> getEditButtons() {
        try {
            return driver.findElements(editButtons);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<WebElement> getDeleteButtons() {
        try {
            return driver.findElements(deleteButtons);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public boolean areEditButtonsVisible() {
        List<WebElement> buttons = getEditButtons();
        if (buttons.isEmpty())
            return false;

        // Check if any button is actually visible and enabled (not disabled)
        for (WebElement btn : buttons) {
            try {
                if (btn.isDisplayed() && btn.isEnabled()) {
                    // Also check for disabled attribute
                    String disabledAttr = btn.getAttribute("disabled");
                    if (disabledAttr == null || disabledAttr.isEmpty()) {
                        return true; // Button is visible and NOT disabled
                    }
                }
            } catch (Exception e) {
                // Button exists but might be stale or hidden
            }
        }
        return false; // All buttons are either hidden or disabled
    }

    public boolean areDeleteButtonsVisible() {
        List<WebElement> buttons = getDeleteButtons();
        if (buttons.isEmpty())
            return false;

        // Check if any button is actually visible and enabled (not disabled)
        for (WebElement btn : buttons) {
            try {
                if (btn.isDisplayed() && btn.isEnabled()) {
                    // Also check for disabled attribute
                    String disabledAttr = btn.getAttribute("disabled");
                    if (disabledAttr == null || disabledAttr.isEmpty()) {
                        return true; // Button is visible and NOT disabled
                    }
                }
            } catch (Exception e) {
                // Button exists but might be stale or hidden
            }
        }
        return false; // All buttons are either hidden or disabled
    }
}
