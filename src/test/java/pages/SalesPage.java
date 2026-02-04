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

    private By emptyStateMessage = By.xpath("//*[contains(text(), 'No sales found')]"); // Generic text match

    // ---------- Page validations ----------
    public boolean isOnSalesPage() {
        return driver.getCurrentUrl().contains("/ui/sales")
                && driver.findElement(pageHeading).getText().equals("Sales");
    }

    public String getPageHeading() {
        return driver.findElement(pageHeading).getText();
    }

    // ---------- Actions ----------
    public void clickSellPlant() {
        driver.findElement(sellPlantButton).click();
    }

    public void clickColumnHeader(String columnName) {
        driver.findElement(By.linkText(columnName)).click();
    }

    // ---------- Table ----------
    public int getSalesCount() {
        List<WebElement> rows = driver.findElements(salesTableRows);
        return rows.size();
    }

    public boolean isSalesTableDisplayed() {
        return driver.findElements(salesTableRows).size() > 0;
    }

    public String getEmptyStateMessage() {
        try {
            return driver.findElement(emptyStateMessage).getText();
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    /**
     * Get list of data for a specific column
     * 
     * @param columnIndex 1-based index of the column
     * @return List of strings containing the column text
     */
    public List<String> getColumnData(int columnIndex) {
        List<WebElement> rows = driver.findElements(salesTableRows);
        List<String> columnData = new java.util.ArrayList<>();
        for (WebElement row : rows) {
            try {
                columnData.add(row.findElement(By.xpath("./td[" + columnIndex + "]")).getText());
            } catch (Exception e) {
                // Ignore bad rows
            }
        }
        return columnData;
    }

    /**
     * Get the count of sales for a specific plant
     * 
     * @param plantName The name of the plant
     * @return Number of sales rows for that plant
     */
    public int getSalesCountForPlant(String plantName) {
        List<WebElement> rows = driver.findElements(salesTableRows);
        int count = 0;
        for (WebElement row : rows) {
            try {
                String plant = row.findElement(By.xpath("./td[1]")).getText();
                if (plant.equalsIgnoreCase(plantName)) {
                    count++;
                }
            } catch (Exception e) {
                // Skip if row doesn't have expected structure
            }
        }
        return count;
    }

    /**
     * Get the most recent sale (first row in table)
     * 
     * @return Array containing [plantName, quantity, totalPrice, soldAt]
     */
    public String[] getMostRecentSale() {
        List<WebElement> rows = driver.findElements(salesTableRows);
        if (rows.isEmpty()) {
            return null;
        }

        WebElement firstRow = rows.get(0);
        String plantName = firstRow.findElement(By.xpath("./td[1]")).getText();
        String quantity = firstRow.findElement(By.xpath("./td[2]")).getText();
        String totalPrice = firstRow.findElement(By.xpath("./td[3]")).getText();
        String soldAt = firstRow.findElement(By.xpath("./td[4]")).getText();

        return new String[] { plantName, quantity, totalPrice, soldAt };
    }

    /**
     * Check if a sale with specific plant and quantity exists in the table
     * 
     * @param plantName Name of the plant
     * @param quantity  Quantity sold
     * @return true if such a sale exists
     */
    public boolean saleExistsForPlantWithQuantity(String plantName, int quantity) {
        List<WebElement> rows = driver.findElements(salesTableRows);
        for (WebElement row : rows) {
            try {
                String plant = row.findElement(By.xpath("./td[1]")).getText();
                String qty = row.findElement(By.xpath("./td[2]")).getText();

                if (plant.equalsIgnoreCase(plantName) && qty.equals(String.valueOf(quantity))) {
                    return true;
                }
            } catch (Exception e) {
                // Skip if row doesn't have expected structure
            }
        }
        return false;
    }

    // ---------- Pagination ----------
    public boolean isPaginationVisible() {
        // Checking if next/prev buttons are present
        return driver.findElements(nextPageBtn).size() > 0 || driver.findElements(previousPageBtn).size() > 0;
    }

    public void clickNextPage() {
        driver.findElement(nextPageBtn).click();
    }

    public void clickPreviousPage() {
        driver.findElement(previousPageBtn).click();
    }

    // ---------- Visibility Checks ----------
    public boolean isSellPlantButtonVisible() {
        return driver.findElements(sellPlantButton).size() > 0 && driver.findElement(sellPlantButton).isDisplayed();
    }

    public boolean isDeleteButtonVisible() {
        return driver.findElements(By.cssSelector("form[action*='/ui/sales/delete'] button")).size() > 0;
    }

    public boolean isDeleteButtonVisibleForAnyRow() {
        List<WebElement> deleteButtons = driver.findElements(By.cssSelector("form[action*='/ui/sales/delete'] button"));
        for (WebElement btn : deleteButtons) {
            if (btn.isDisplayed()) {
                return true;
            }
        }
        return false;
    }
}