package stepdefinitions.api;

import dto.PlantSummaryDTO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import utils.ApiBase;

public class UserApiSteps extends ApiBase {

    @Given("I am authenticated as a User")
    public void iAmAuthenticatedAsAUser() {
        request = getUserRequestSpec();
    }

    @Then("the response should match the plant summary structure")
    public void theResponseShouldMatchThePlantSummaryStructure() {
        PlantSummaryDTO summary = response.as(PlantSummaryDTO.class);
        Assert.assertNotNull("Total Plants should not be null", summary.getTotalPlants());
        Assert.assertNotNull("Plants list should not be null", summary.getPlants());
    }
}
