package stepdefinitions.ui;

import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import pages.PlantsPage;
import utils.DriverFactory;

public class PlantsUserSteps {

    WebDriver driver;
    PlantsPage plantsPage;

    private void refreshPages() {
        DriverFactory.initDriver();
        this.driver = DriverFactory.getDriver();
        if (driver != null) {
            plantsPage = new PlantsPage(driver);
        }
    }

    @Given("user navigates to plants page")
    public void user_navigates_to_plants_page() {
        refreshPages();
        if (driver == null) {
            Assert.fail("Driver is null. Make sure user is logged in before this step.");
        }
        driver.get("http://localhost:8080/ui/plants");
        plantsPage = new PlantsPage(driver);
    }

    @Then("user should not see add plant button")
    public void user_should_not_see_add_plant_button() {
        refreshPages();
        Assert.assertFalse("Add plant button should not be visible for users",
                plantsPage.isAddPlantButtonVisible());
    }

    @Given("plants exist")
    public void plants_exist() {
        // Assume plants exist in the database
        System.out.println("⚠️ Assuming plants exist in database");
    }

    @Then("user should not see edit action")
    public void user_should_not_see_edit_action() {
        refreshPages();
        Assert.assertFalse("Edit action should not be visible for users",
                plantsPage.isEditActionVisible());
    }

    @Then("user should not see delete action")
    public void user_should_not_see_delete_action() {
        refreshPages();
        Assert.assertFalse("Delete action should not be visible for users",
                plantsPage.isDeleteActionVisible());
    }

    @Given("multiple plants exist")
    public void multiple_plants_exist() {
        // Assume multiple plants exist in the database
        System.out.println("⚠️ Assuming multiple plants exist in database");
    }

    @Given("plants exist in different categories")
    public void plants_exist_in_different_categories() {
        // Assume plants exist in different categories
        System.out.println("⚠️ Assuming plants exist in different categories");
    }
}
