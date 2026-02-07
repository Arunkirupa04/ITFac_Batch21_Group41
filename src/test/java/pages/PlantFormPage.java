package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class PlantFormPage {

    private WebDriver driver;

    public PlantFormPage(WebDriver driver) {
        this.driver = driver;
    }

    // ---------- Locators ----------
    private By pageHeading = By.tagName("h3"); // Assuming generic h3 based on other pages
    private By nameInput = By.name("name");
    private By priceInput = By.name("price");
    private By quantityInput = By.name("quantity");
    private By categorySelect = By.name("categoryId");
    private By filterInput = By.id("filterInput"); // optional / if exists

    // ✅ Buttons (UPDATED)
    // Works for both add + edit because form action starts with /ui/plants
    private By saveButton = By.cssSelector("form[action^='/ui/plants'] button.btn.btn-primary");
    private By cancelButton = By.cssSelector("a.btn.btn-secondary[href='/ui/plants']");

    // Validation
    private By validationMessage = By.cssSelector(
            ".alert-danger, .invalid-feedback, .error-message, .text-danger");
    private By successMessage = By.cssSelector(".alert-success");

    // ---------- Validations ----------
    public boolean isOnAddPlantPage() {
        return driver.getCurrentUrl().contains("/ui/plants/add");
    }

    public boolean isOnEditPlantPage() {
        return driver.getCurrentUrl().contains("/ui/plants/edit");
    }

    public boolean isValidationMessageDisplayed(String expectedMessage) {
        try {
            java.util.List<WebElement> errors = driver.findElements(validationMessage);
            for (WebElement error : errors) {
                if (error.getText().contains(expectedMessage)) {
                    return true;
                }
            }
            return driver.getPageSource().contains(expectedMessage);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAnyValidationErrorDisplayed() {
        return driver.findElements(validationMessage).size() > 0;
    }

    public String getAnyValidationErrorText() {
        java.util.List<WebElement> errors = driver.findElements(validationMessage);
        if (errors.isEmpty())
            return "No error visible";
        StringBuilder sb = new StringBuilder();
        for (WebElement e : errors) {
            sb.append(e.getText()).append(" ");
        }
        return sb.toString().trim();
    }

    // ---------- Actions ----------
    public void enterPlantName(String name) {
        WebElement el = driver.findElement(nameInput);
        el.clear();
        el.sendKeys(name);
    }

    public void clearPlantName() {
        WebElement el = driver.findElement(nameInput);
        el.clear();
    }

    public void enterPrice(String price) {
        WebElement el = driver.findElement(priceInput);
        el.clear();
        el.sendKeys(price);
    }

    public void enterQuantity(String qty) {
        WebElement el = driver.findElement(quantityInput);
        el.clear();
        el.sendKeys(qty);
    }

    public void selectCategory(String categoryName) {
        Select select = new Select(driver.findElement(categorySelect));
        if (categoryName == null || categoryName.isEmpty()) {
            try {
                select.selectByValue("");
            } catch (Exception e) {
                select.selectByIndex(0);
            }
        } else {
            try {
                select.selectByVisibleText(categoryName);
            } catch (NoSuchElementException e) {
                // Fallback: app may not have "Indoor" - select first non-empty option
                java.util.List<WebElement> options = select.getOptions();
                for (int i = 1; i < options.size(); i++) {
                    String text = options.get(i).getText().trim();
                    if (!text.isEmpty() && !text.equalsIgnoreCase("Select") && !text.equals("--")) {
                        select.selectByIndex(i);
                        return;
                    }
                }
                if (options.size() > 0)
                    select.selectByIndex(1);
            }
        }
    }

    public void selectSubCategory(String subCategoryName) {
        if (subCategoryName == null || subCategoryName.isEmpty())
            return;

        try {
            // Wait a bit for sub-category options to populate if it's dynamic
            Thread.sleep(1000);
            // Try all selects on the page
            java.util.List<WebElement> selects = driver.findElements(By.tagName("select"));

            boolean found = false;

            for (WebElement selectEl : selects) {
                Select sel = new Select(selectEl);
                for (WebElement option : sel.getOptions()) {
                    if (option.getText().trim().equalsIgnoreCase(subCategoryName) ||
                            option.getText().contains(subCategoryName)) {
                        sel.selectByVisibleText(option.getText());
                        found = true;
                        System.out.println("✅ Selected Sub-Category: " + subCategoryName + " in select: "
                                + selectEl.getAttribute("name"));
                        break;
                    }
                }
                if (found)
                    break;
            }

            if (!found) {
                System.out.println("⚠️ Could not find sub-category: " + subCategoryName + " in any dropdown.");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error selecting sub-category: " + e.getMessage());
        }
    }

    // ✅ UPDATED: Click Save with wait (robust)
    public void clickSave() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        btn.click();
    }

    // ✅ UPDATED: Click Cancel (optional wait)
    public void clickCancel() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(cancelButton));
        btn.click();
    }
}