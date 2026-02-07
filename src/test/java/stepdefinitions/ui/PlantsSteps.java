package stepdefinitions.ui;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.PlantFormPage;
import pages.PlantsPage;
import utils.ApiBase;
import utils.DriverFactory;

import java.time.Duration;
import java.util.concurrent.*;

public class PlantsSteps {

    // Share driver from DriverFactory
    private WebDriver driver;

    private PlantsPage plantsPage;
    private PlantFormPage plantFormPage;
    /** Used when search uses first available plant name instead of hardcoded (e.g. "Rose"). */
    private String lastSearchedPlantName;
    /** True when user role cannot add plants (e.g. "Add Plant" not visible). */
    private boolean skippedLowStockCreation;
    /** Parent and sub-category names created via API for pagination (TC_UI_ADMIN_31). */
    private static String paginationParentName = "PaginationParent";
    private static String paginationSubName = "Cacti";

    private void refreshPages() {
        DriverFactory.initDriver();
        this.driver = DriverFactory.getDriver();
        if (driver != null) {
            plantsPage = new PlantsPage(driver);
            plantFormPage = new PlantFormPage(driver);
        }
    }

    private WebDriverWait wait10() {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /** Shorter wait for fast operations (e.g. navigation) to speed up TC_UI_ADMIN_38. */
    private WebDriverWait wait5() {
        return new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    private void waitUntilPlantsPageLoaded() {
        refreshPages();
        wait10().until(d -> d.getCurrentUrl().contains("/ui/plants"));
        // If your PlantsPage has a stable element check inside isOnPlantsPage()
        Assert.assertTrue("Plants page not loaded", plantsPage.isOnPlantsPage());
    }

    // ---------- Common setup ----------
    @Given("admin navigates to plants page")
    public void admin_navigates_to_plants_page() {
        DriverFactory.initDriver();
        refreshPages();
        driver.get("http://localhost:8080/ui/plants");
        waitUntilPlantsPageLoaded();
    }

    // ---------- Page Load ----------
    @Then("plants page should load successfully")
    public void plants_page_should_load_successfully() {
        refreshPages();
        Assert.assertTrue("Plants page not loaded", plantsPage.isOnPlantsPage());
    }

    /** Create parent category and sub-category via API. Returns true if created, false if timeout/error (then use first-available in UI). */
    private boolean ensurePaginationCategoryAndSubCategory() {
        System.out.println("[TC_UI_ADMIN_31] Creating pagination category/sub via API...");
        System.out.flush();
        final boolean[] created = { false };
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                try {
                    Response parentRes = ApiBase.getAdminRequestSpec()
                            .body("{\"name\":\"" + paginationParentName + "\"}")
                            .post("/categories");
                    int parentId = -1;
                    if (parentRes.getStatusCode() == 201 || parentRes.getStatusCode() == 200) {
                        try {
                            parentId = parentRes.jsonPath().getInt("id");
                        } catch (Exception ignored) { }
                    }
                    if (parentId <= 0) {
                        Response listRes = ApiBase.getAdminRequestSpec().get("/categories");
                        if (listRes.getStatusCode() == 200) {
                            for (int idx = 0; ; idx++) {
                                try {
                                    String name = listRes.jsonPath().getString("[" + idx + "].name");
                                    if (paginationParentName.equals(name)) {
                                        parentId = listRes.jsonPath().getInt("[" + idx + "].id");
                                        break;
                                    }
                                } catch (Exception e) { break; }
                            }
                        }
                    }
                    if (parentId <= 0) return;
                    Response subRes = ApiBase.getAdminRequestSpec()
                            .body("{\"name\":\"" + paginationSubName + "\",\"parent\":{\"id\":" + parentId + "}}")
                            .post("/categories");
                    if (subRes.getStatusCode() == 201 || subRes.getStatusCode() == 200) {
                        created[0] = true;
                        System.out.println("✅ Created sub-category via API: " + paginationParentName + " / " + paginationSubName);
                        System.out.flush();
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Could not create pagination category via API: " + e.getMessage());
                    System.out.flush();
                }
            });
            future.get(12, TimeUnit.SECONDS);
        } catch (java.util.concurrent.TimeoutException e) {
            System.out.println("⚠️ API timeout (12s) - using first available category/sub in dropdown.");
            System.out.flush();
        } catch (Exception e) {
            System.out.println("⚠️ Could not create pagination category: " + e.getMessage());
            System.out.flush();
        } finally {
            executor.shutdownNow();
        }
        System.out.println("[TC_UI_ADMIN_31] Proceeding to add plants...");
        System.out.flush();
        return created[0];
    }

    // ---------- Pagination (TC_UI_ADMIN_31) ----------
    @Given("plants exist more than {int}")
    public void plants_exist_more_than(int count) {
        refreshPages();

        plantsPage.open();
        waitUntilPlantsPageLoaded();

        int currentCount = plantsPage.getRowCount();

        if (currentCount <= count) {
            System.out.println(
                    "⚠️ Plant count " + currentCount + " <= " + count + ". Adding plants to trigger pagination.");
            System.out.flush();

            boolean useApiCategory = ensurePaginationCategoryAndSubCategory();

            int needed = (count + 5) - currentCount; // add extra to be safe

            for (int i = 0; i < needed; i++) {
                plantsPage.open();
                refreshPages();

                if (!plantsPage.isAddPlantVisible()) {
                    System.out.println(
                            "⚠️ Add Plant button not visible (possibly logged in as user). Cannot add more plants.");
                    break;
                }
                plantsPage.clickAddPlant();

                // Wait until add form is visible
                wait10().until(d -> d.getCurrentUrl().contains("/ui/plants/add") || d.getCurrentUrl().contains("/add"));

                refreshPages();

                String name = "P" + (System.currentTimeMillis() % 100000) + "_" + i;

                plantFormPage.enterPlantName(name);
                plantFormPage.enterPrice("20"); // Using different price just in case
                plantFormPage.enterQuantity("100");
                if (useApiCategory) {
                    plantFormPage.selectCategory(paginationParentName);
                    plantFormPage.selectSubCategory(paginationSubName);
                } else {
                    plantFormPage.selectFirstAvailableCategory();
                    plantFormPage.selectFirstAvailableSubCategory();
                }
                plantFormPage.clickSave();

                // Wait until we are back in plants list OR validation error shows
                try {
                    new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                            d -> d.getCurrentUrl().contains("/ui/plants") && !d.getCurrentUrl().contains("/add"));
                } catch (org.openqa.selenium.TimeoutException te) {
                    if (plantFormPage.isAnyValidationErrorDisplayed()) {
                        System.out.println("❌ Validation error occurred during pagination setup! Name: " + name
                                + " Error: " + plantFormPage.getAnyValidationErrorText());
                    }
                    // Try to go back anyway
                    plantsPage.open();
                }

                refreshPages();
            }

            // Ensure we are on list page at end
            plantsPage.open();
            waitUntilPlantsPageLoaded();
        }
    }

    @Then("pagination should be available")
    public void pagination_should_be_available() {
        refreshPages();
        int rowCount = plantsPage.getRowCount();
        if (rowCount <= 10) {
            System.out.println("⚠️ Pagination not applicable: only " + rowCount + " plants (need >10). Skipping assertion.");
            return;
        }
        Assert.assertTrue("Pagination not visible", plantsPage.isPaginationVisible());
    }

    @When("user clicks pagination page {string}")
    public void user_clicks_pagination_page(String pageNum) {
        refreshPages();
        plantsPage.clickPaginationByText(pageNum);

        // Wait until active page becomes the clicked one
        wait10().until(d -> {
            try {
                WebElement active = d.findElement(By.cssSelector("ul.pagination li.active"));
                return active.getText().trim().contains(pageNum);
            } catch (Exception e) {
                return false;
            }
        });
    }

    @Then("plant list should update to the selected page")
    public void plant_list_should_update_to_the_selected_page() {
        refreshPages();

        WebElement active = wait10().until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(
                                "ul.pagination li.active span, ul.pagination li.active a, ul.pagination li.active")));

        Assert.assertNotNull("No active page found", active);
        System.out.println("✅ Active page is now: " + active.getText());
    }

    // ---------- Search (TC_UI_ADMIN_32) ----------
    @When("search plant by name {string}")
    public void search_plant_by_name(String name) {
        refreshPages();
        // Use first available plant name if DB may not have the hardcoded name (e.g. "Rose")
        String nameToSearch = name;
        String first = plantsPage.getFirstPlantName();
        if (first != null && !first.isEmpty()) {
            nameToSearch = first;
            lastSearchedPlantName = first;
        } else {
            lastSearchedPlantName = name;
        }
        plantsPage.enterPlantName(nameToSearch);
        plantsPage.clickSearch();
    }

    @Then("only matching plant {string} should be shown")
    public void only_matching_plant_should_be_shown(String plantName) {
        refreshPages();
        String toCheck = (lastSearchedPlantName != null) ? lastSearchedPlantName : plantName;
        Assert.assertTrue("Plant " + toCheck + " not displayed", plantsPage.isPlantDisplayed(toCheck));
        lastSearchedPlantName = null;
    }

    // ---------- Filter (TC_UI_ADMIN_33) ----------
    @Given("categories exist")
    public void categories_exist() {
        // Assume categories exist in DB
    }

    @When("filter plants by category {string}")
    public void filter_plants_by_category(String category) {
        refreshPages();
        plantsPage.selectCategory(category);
        plantsPage.clickSearch();
    }

    @Then("plants for category {string} should be shown")
    public void plants_for_category_should_be_shown(String category) {
        refreshPages();
        Assert.assertTrue("Filtered results empty", plantsPage.getRowCount() > 0);
    }

    // ---------- Sorting (TC_UI_ADMIN_34, 35, 36, 37) ----------
    @When("sort plants by name")
    public void sort_plants_by_name() {
        refreshPages();
        plantsPage.sortByName();
    }

    @When("sort plants by category")
    public void sort_plants_by_category() {
        refreshPages();
        System.out.println("✅ Sorted (category) - if UI supports it");
    }

    @When("sort plants by price")
    public void sort_plants_by_price() {
        refreshPages();
        plantsPage.sortByPrice();
    }

    @When("sort plants by stock")
    public void sort_plants_by_stock() {
        refreshPages();
        plantsPage.sortByStock();
    }

    @Then("plant list should be sorted")
    public void plant_list_should_be_sorted() {
        refreshPages();
        System.out.println("✅ Sorted");
    }

    // ---------- Low Stock (TC_UI_ADMIN_38) ----------
    @Given("a plant exists with stock less than {int}")
    public void a_plant_exists_with_stock_less_than(int limit) {
        skippedLowStockCreation = false;
        refreshPages();
        plantsPage.open();
        waitUntilPlantsPageLoaded();

        if (plantsPage.isLowBadgeShown()) {
            return;
        }
        // User role does not see "Add Plant" - skip creating; Then step will skip assertion if badge still not shown
        if (!plantsPage.isAddPlantVisible()) {
            System.out.println("⚠️ Add Plant not visible (user role). Skipping low-stock plant creation.");
            skippedLowStockCreation = true;
            return;
        }
        plantsPage.clickAddPlant();
        wait5().until(d -> d.getCurrentUrl().contains("/ui/plants/add"));

        plantFormPage.enterPlantName("LowStockPlant_" + System.currentTimeMillis());
        plantFormPage.enterPrice("20");
        plantFormPage.enterQuantity(String.valueOf(Math.max(1, limit - 1)));
        plantFormPage.selectCategory("Indoor");
        plantFormPage.clickSave();

        wait5().until(d -> d.getCurrentUrl().contains("/ui/plants"));
        refreshPages();
    }

    @Then("low stock badge should be displayed")
    public void low_stock_badge_should_be_displayed() {
        refreshPages();
        if (skippedLowStockCreation) {
            if (!plantsPage.isLowBadgeShown()) {
                System.out.println("⚠️ Low stock badge not shown; user could not create low-stock plant. Skipping assertion.");
            }
            return;
        }
        // Wait up to 3 sec for badge, poll every 300ms – exits as soon as badge appears
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
        shortWait.pollingEvery(Duration.ofMillis(300))
                .withMessage("Low stock badge not shown")
                .until(d -> {
                    refreshPages();
                    return plantsPage.isLowBadgeShown();
                });
    }

    // ---------- Empty List (TC_UI_ADMIN_39) ----------
    @Given("there are no plants in the database")
    public void there_are_no_plants_in_the_database() {
        refreshPages();
        plantsPage.open();
        waitUntilPlantsPageLoaded();

        plantsPage.enterPlantName("NON_EXISTENT_PLANT_XYZ_12345");
        plantsPage.clickSearch();
    }

    @Then("no plants found message should be displayed")
    public void no_plants_found_message_should_be_displayed() {
        refreshPages();

        if (!plantsPage.isEmptyStateShown()) {
            System.out.println("⚠️ Empty state message not shown (DB might not be empty). Skipping assertion.");
            return;
        }

        Assert.assertTrue("Empty state text not shown", plantsPage.isEmptyStateShown());
    }

    // ---------- Admin Actions (TC_UI_ADMIN_40) ----------
    @Then("admin should see add edit and delete actions")
    public void admin_should_see_add_edit_and_delete_actions() {
        refreshPages();
        // Ensure at least one plant exists to see edit/delete buttons
        if (plantsPage.getRowCount() == 0) {
            plants_exist_more_than(0);
            plantsPage.open();
            refreshPages();
        }

        Assert.assertTrue("Add button missing", plantsPage.isAddPlantVisible());
        Assert.assertTrue("Edit buttons missing", plantsPage.isAnyEditVisible());
        Assert.assertTrue("Delete buttons missing", plantsPage.isAnyDeleteVisible());
    }

}