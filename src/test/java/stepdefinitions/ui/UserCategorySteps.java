package stepdefinitions.ui;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.*;
import pages.AddCategoryPage;
import pages.CategoriesPage;
import pages.EditCategoryPage;
import utils.DriverFactory;

import java.util.List;

public class UserCategorySteps {

    private WebDriver getDriver() {
        DriverFactory.initDriver();
        return DriverFactory.getDriver();
    }

    CategoriesPage categoriesPage;
    AddCategoryPage addCategoryPage;
    EditCategoryPage editCategoryPage;

    // ========== Helper Methods ==========

    private void loginAsUser() {
        getDriver().get("http://localhost:8080/ui/login");

        // Login as testuser
        getDriver().findElement(By.name("username")).sendKeys("testuser");
        getDriver().findElement(By.name("password")).sendKeys("test123");
        getDriver().findElement(By.xpath("//button[@type='submit']")).click();

        try {
            Thread.sleep(2000); // Wait for dashboard
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void navigateToCategoriesPage() {
        getDriver().get("http://localhost:8080/ui/categories");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        categoriesPage = new CategoriesPage(getDriver());
    }

    // ========== Given Steps ==========

    @Given("User is logged in and on categories page")
    public void user_is_logged_in_and_on_categories_page() {
        loginAsUser();
        navigateToCategoriesPage();
    }

    @Given("User is logged in")
    public void user_is_logged_in() {
        loginAsUser();
    }

    // ========== When Steps ==========

    @When("User navigates to \\/ui\\/categories")
    public void user_navigates_to_categories() {
        navigateToCategoriesPage();
    }

    @When("User enters search keyword {string}")
    public void user_enters_search_keyword(String keyword) {
        categoriesPage.enterSearchKeyword(keyword);
    }

    @When("User clicks Search button")
    public void user_clicks_search_button() {
        categoriesPage.clickSearchButton();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @When("User selects parent category {string}")
    public void user_selects_parent_category(String parentName) {
        categoriesPage.selectParentCategory(parentName);
    }

    @When("User clicks Reset button")
    public void user_clicks_reset_button() {
        categoriesPage.clickResetButton();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @When("User clicks ID column header")
    public void user_clicks_id_column_header() {
        categoriesPage.clickIdColumnHeader();
    }

    @When("User clicks Name column header")
    public void user_clicks_name_column_header() {
        categoriesPage.clickNameColumnHeader();
    }

    @When("User clicks Parent column header")
    public void user_clicks_parent_column_header() {
        categoriesPage.clickParentColumnHeader();
    }

    @When("User clicks page {int}")
    public void user_clicks_page(int pageNumber) {
        try {
            categoriesPage.clickPaginationPage(pageNumber);
            Thread.sleep(1000);
        } catch (NoSuchElementException e) {
            System.out.println("⚠️ Pagination not available");
        } catch (Exception e) {
            System.out.println("⚠️ Error clicking pagination: " + e.getMessage());
        }
    }

    @When("User attempts to access {string}")
    public void user_attempts_to_access(String url) {
        getDriver().get("http://localhost:8080" + url);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // ========== Then Steps ==========

    @Then("Categories list should be displayed if data exists")
    public void categories_list_should_be_displayed_if_data_exists() {
        if (categoriesPage == null)
            categoriesPage = new CategoriesPage(getDriver());
        Assert.assertTrue("Categories table not displayed", categoriesPage.isTableDisplayed());
    }

    @Then("{string} message should be displayed")
    public void message_should_be_displayed(String message) {
        if (categoriesPage == null)
            categoriesPage = new CategoriesPage(getDriver());
        Assert.assertTrue("Empty message not displayed",
                categoriesPage.isTableEmpty() && categoriesPage.getEmptyStateMessage().contains(message));
    }

    @Then("{string} button should not be visible")
    public void button_should_not_be_visible(String buttonName) {
        if (categoriesPage == null)
            categoriesPage = new CategoriesPage(getDriver());
        Assert.assertFalse("Button should be hidden", categoriesPage.isAddCategoryButtonVisible());
    }

    @Then("Edit buttons should not be visible for any category")
    public void edit_buttons_should_not_be_visible() {
        if (categoriesPage == null)
            categoriesPage = new CategoriesPage(getDriver());
        Assert.assertFalse("Edit buttons should be hidden/disabled", categoriesPage.areEditButtonsVisible());
    }

    @Then("Delete buttons should not be visible for any category")
    public void delete_buttons_should_not_be_visible() {
        if (categoriesPage == null)
            categoriesPage = new CategoriesPage(getDriver());
        Assert.assertFalse("Delete buttons should be hidden/disabled", categoriesPage.areDeleteButtonsVisible());
    }

    @Then("Table should be sorted by Name in {string} order")
    public void table_should_be_sorted_by_name_in_order(String order) {
        if (categoriesPage == null)
            categoriesPage = new CategoriesPage(getDriver());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<String> names = categoriesPage.getCategoryNames();
        if (names.size() <= 1)
            return;

        if (order.equalsIgnoreCase("any")) {
            Assert.assertTrue("Not sorted",
                    categoriesPage.isSortedByName("ascending") || categoriesPage.isSortedByName("descending"));
            return;
        }

        Assert.assertTrue("Not sorted correctly", categoriesPage.isSortedByName(order));
    }

    @Then("Table should be sorted by Parent in {string} order")
    public void table_should_be_sorted_by_parent_in_order(String order) {
        if (categoriesPage == null)
            categoriesPage = new CategoriesPage(getDriver());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<String> parents = categoriesPage.getParentNames();
        if (parents.size() <= 1)
            return;

        if (order.equalsIgnoreCase("any")) {
            Assert.assertTrue("Not sorted",
                    categoriesPage.isSortedByParent("ascending") || categoriesPage.isSortedByParent("descending"));
            return;
        }

        Assert.assertTrue("Not sorted correctly", categoriesPage.isSortedByParent(order));
    }

    @Then("User should be blocked from accessing the page")
    public void user_should_be_blocked_from_accessing_the_page() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String currentUrl = getDriver().getCurrentUrl();
        String pageSource = getDriver().getPageSource().toLowerCase();

        boolean isForbidden = pageSource.contains("403") ||
                pageSource.contains("forbidden") ||
                pageSource.contains("access denied");

        boolean redirected = !currentUrl.contains("/add") && !currentUrl.contains("/edit");

        Assert.assertTrue("User not blocked", isForbidden || redirected);
    }

    @Then("User should not see Add Category form")
    public void user_should_not_see_add_category_form() {
        addCategoryPage = new AddCategoryPage(getDriver());
        if (addCategoryPage.isOnAddCategoryPage()) {
            Assert.assertTrue("Access denied missing", getDriver().getPageSource().toLowerCase().contains("403")
                    || getDriver().getPageSource().toLowerCase().contains("forbidden"));
        }
    }

    @Then("User should not see Edit Category form")
    public void user_should_not_see_edit_category_form() {
        editCategoryPage = new EditCategoryPage(getDriver());
        if (editCategoryPage.isOnEditCategoryPage()) {
            Assert.assertTrue("Access denied missing", getDriver().getPageSource().toLowerCase().contains("403")
                    || getDriver().getPageSource().toLowerCase().contains("forbidden"));
        }
    }
}
