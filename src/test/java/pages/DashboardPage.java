package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class DashboardPage {

    WebDriver driver;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    // ---------- Locators ----------
    private By pageHeading = By.tagName("h2"); // Assuming "QA Training Application" or similar h2
    private By sidebarLinks = By.cssSelector(".sidebar .nav-link");
    private By summaryCards = By.className("card"); // Assumption for dashboard cards

    // ---------- Navigation ----------
    public boolean isOnDashboardPage() {
        return driver.getCurrentUrl().contains("/ui/dashboard");
    }

    // ---------- Sidebar ----------
    public void clickSidebarLink(String linkName) {
        List<WebElement> links = driver.findElements(sidebarLinks);
        for (WebElement link : links) {
            if (link.getText().trim().contains(linkName)) {
                link.click();
                return;
            }
        }
        throw new RuntimeException("Sidebar link not found: " + linkName);
    }

    public boolean isSidebarLinkHighlighted(String linkName) {
        List<WebElement> links = driver.findElements(sidebarLinks);
        for (WebElement link : links) {
            if (link.getText().trim().contains(linkName)) {
                String classes = link.getAttribute("class");
                return classes.contains("active");
            }
        }
        return false;
    }

    // ---------- Summary Data ----------
    /**
     * Tries to find a number associated with a label in the dashboard.
     * Assumes cards like: [Icon] [Count] [Label] or similar.
     * We will search for an element containing the label, then look for a number
     * near it.
     */
    public int getSummaryCount(String label) {
        // This is tricky without HTML.
        // Strategy: Find an element containing the label (e.g. "Categories"), then find
        // a sibling or child with a number.
        // Or find all cards and check text.

        List<WebElement> cards = driver.findElements(summaryCards);
        if (cards.isEmpty()) {
            // Fallback: search for any element with the label text
            // This might pick up the sidebar link, so be careful.
            // We'll look for h5 or h3 or div typically used in cards.
            return -1;
        }

        for (WebElement card : cards) {
            String text = card.getText();
            if (text.contains(label)) {
                // Extract digits
                // "Total Plants 10" -> 10
                String numberOnly = text.replaceAll("[^0-9]", "");
                if (!numberOnly.isEmpty()) {
                    return Integer.parseInt(numberOnly);
                }
            }
        }
        return -1;
    }
}
