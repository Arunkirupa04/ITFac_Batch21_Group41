package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

public class AddCategoryPage {

    WebDriver driver;

    public AddCategoryPage(WebDriver driver) {
        this.driver = driver;
    }

    // ---------- Locators ----------
    private By pageHeading = By.xpath("//h3[contains(text(),'Add Category') or contains(text(),'Category')]");
    private By nameInput = By.name("name");
    private By parentDropdown = By.name("parentId");
    private By saveButton = By.xpath("//button[contains(text(),'Save') or @type='submit']");
    private By cancelButton = By.linkText("Cancel");
    private By validationMessage = By.cssSelector(".alert-danger, .invalid-feedback, .error-message");
    private By successMessage = By.cssSelector(".alert-success");

    // ---------- Page Validations ----------
    public boolean isOnAddCategoryPage() {
        try {
            return driver.getCurrentUrl().contains("/ui/categories/add");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidationMessageDisplayed() {
        try {
            WebElement message = driver.findElement(validationMessage);
            return message.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public String getValidationMessage() {
        try {
            return driver.findElement(validationMessage).getText();
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            return driver.findElement(successMessage).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // ---------- Actions ----------
    public void enterCategoryName(String name) {
        WebElement nameField = driver.findElement(nameInput);
        nameField.clear();
        nameField.sendKeys(name);
    }

    public void clearCategoryName() {
        WebElement nameField = driver.findElement(nameInput);
        nameField.clear();
    }

    public void selectParentCategory(String parentName) {
        Select select = new Select(driver.findElement(parentDropdown));
        select.selectByVisibleText(parentName);
    }

    public void clickSaveButton() {
        WebElement button = driver.findElement(saveButton);
        // Check if button is enabled before clicking
        if (!button.isEnabled()) {
            System.out.println("⚠️ WARNING: Save button is DISABLED!");
        }
        // Check for any validation errors before submitting
        if (isValidationMessageDisplayed()) {
            System.out.println("⚠️ WARNING: Validation error present: " + getValidationMessage());
        }
        button.click();
        System.out.println("🔘 Save button clicked");
    }

    public void clickCancelButton() {
        driver.findElement(cancelButton).click();
    }

    public boolean isParentDropdownEnabled() {
        return driver.findElement(parentDropdown).isEnabled();
    }

    public String getNameInputValue() {
        return driver.findElement(nameInput).getAttribute("value");
    }
}
