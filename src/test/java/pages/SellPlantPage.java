package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

public class SellPlantPage {

    WebDriver driver;

    public SellPlantPage(WebDriver driver) {
        this.driver = driver;
    }

    // ---------- Locators ----------
    private By plantDropdown = By.id("plantId");
    private By quantityInput = By.id("quantity");
    private By sellButton = By.cssSelector("button.btn.btn-primary");
    private By cancelButton = By.linkText("Cancel");
    private By pageHeading = By.tagName("h3");

    // ---------- Actions ----------
    public void selectPlant(String plantName) {
        Select select = new Select(driver.findElement(plantDropdown));
        select.selectByVisibleText(plantName);
    }

    public void enterQuantity(String quantity) {
        WebElement qty = driver.findElement(quantityInput);
        qty.clear();
        qty.sendKeys(quantity);
    }

    public void clickSell() {
        driver.findElement(sellButton).click();
    }

    public void clickCancel() {
        driver.findElement(cancelButton).click();
    }

    // ---------- Validations ----------
    public boolean isOnSellPlantPage() {
        return driver.getCurrentUrl().contains("/ui/sales")
                && driver.findElement(pageHeading).getText().contains("Sell Plant");
    }
}
