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
    private Select getPlantSelect() {
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(plantDropdown));
        return new Select(driver.findElement(plantDropdown));
    }

    public void selectPlant(String plantName) {
        getPlantSelect().selectByVisibleText(plantName);
    }

    public void enterQuantity(String quantity) {
        WebElement qty = driver.findElement(quantityInput);
        qty.clear();
        qty.sendKeys(quantity);
    }

    /**
     * Clicks the Sell button. After this, if the sale is successful, the app
     * navigates to the Sales page.
     * There is no success message on SellPlantPage.
     */
    public void clickSell() {
        driver.findElement(sellButton).click();
        // No need to check for a message here; navigation is handled in the test step.
    }

    public void clickCancel() {
        driver.findElement(cancelButton).click();
    }

    // ---------- Validations ----------
    public boolean isOnSellPlantPage() {
        return driver.getCurrentUrl().contains("/ui/sales")
                && driver.findElement(pageHeading).getText().contains("Sell Plant");
    }

    // ---------- Stock Extraction ----------
    /**
     * Extracts the current stock value for a given plant from the dropdown
     * 
     * @param plantName The base name of the plant (e.g., "Lemon")
     * @return The stock value as an integer, or -1 if plant not found
     */
    public int extractStockFromOption(String plantName) {
        Select select = getPlantSelect();

        for (WebElement option : select.getOptions()) {
            String optionText = option.getText();
            // Match "PlantName (Stock: X)" exactly at the start
            if (optionText.startsWith(plantName + " (") || optionText.equals(plantName)) {
                // Extract number from format like "Lemon (Stock: 10)"
                // Using regex to capture the number after "Stock:"
                String stockStr = optionText.replaceAll(".*Stock:\\s*(\\d+).*", "$1");
                try {
                    return Integer.parseInt(stockStr);
                } catch (NumberFormatException e) {
                    System.err.println("Failed to parse stock for: " + optionText);
                    return -1;
                }
            }
        }
        System.err.println("Plant not found in dropdown: " + plantName);
        return -1; // Plant not found
    }

    /**
     * Gets the full text of the selected plant option
     * 
     * @return The text of the currently selected option (e.g., "Lemon (Stock: 10)")
     */
    public String getSelectedPlantText() {
        return getPlantSelect().getFirstSelectedOption().getText();
    }

    /**
     * Gets all available plants in the dropdown
     * 
     * @return Array of option texts
     */
    public String[] getAllPlantOptions() {
        return getPlantSelect().getOptions().stream()
                .map(WebElement::getText)
                .toArray(String[]::new);
    }
}