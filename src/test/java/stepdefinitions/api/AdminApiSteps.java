package stepdefinitions.api;

import dto.Plant;
import dto.PlantEditResponseDTO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import org.junit.Assert;
import utils.ApiBase;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class AdminApiSteps extends ApiBase {

    private int plantId;
    private int categoryId;
    private Plant plantRequest;

    @Given("I am authenticated as an Admin")
    public void iAmAuthenticatedAsAnAdmin() {
        request = getAdminRequestSpec();
    }

    @Given("I try to get a plant with invalid id {int}")
    public void iTryToGetAPlantWithInvalidId(int id) {
        this.plantId = id;
    }

    @When("I send a GET request to {string}")
    public void iSendAGETRequestTo(String endpoint) {
        if (endpoint.contains("{id}")) {
            endpoint = endpoint.replace("{id}", String.valueOf(plantId));
        }
        // Handle generic endpoints or specific ones if needed, relying on direct path
        // for now
        response = request.get(endpoint);
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Then("the response status code should be {int} or {int}")
    public void theResponseStatusCodeShouldBeOr(int status1, int status2) {
        int actualStatus = response.getStatusCode();
        Assert.assertTrue("Status code should be " + status1 + " or " + status2 + " but was " + actualStatus,
                actualStatus == status1 || actualStatus == status2);
    }

    @Then("the error message should be {string}")
    public void theErrorMessageShouldBe(String expectedMessage) {
        // Assuming error response has a "message" field or similar
        // Adjust jsonPath based on actual API error structure
        String actualMessage = response.jsonPath().getString("message");
        // fallback if plain text
        if (actualMessage == null)
            actualMessage = response.getBody().asString();

        Assert.assertTrue("Error message mismatched: " + actualMessage, actualMessage.contains(expectedMessage));
    }

    @Given("I have a valid plant id to update")
    public void iHaveAValidPlantIdToUpdate() {
        this.plantId = 1; // Assuming 1 exists
    }

    @Given("I provide valid update data with name {string}, price {double}, and quantity {int}")
    public void iProvideValidUpdateData(String name, double price, int quantity) {
        plantRequest = new Plant(name, price, quantity);
        request.body(plantRequest);
    }

    @When("I send a PUT request to update the plant")
    public void iSendAPUTRequestToUpdateThePlant() {
        response = request.put("/plants/" + plantId);
    }

    @Then("the response should match the updated plant data")
    public void theResponseShouldMatchTheUpdatedPlantData() {
        PlantEditResponseDTO updatedPlant = response.as(PlantEditResponseDTO.class);
        Assert.assertEquals(plantRequest.getName(), updatedPlant.getName());
        Assert.assertEquals(plantRequest.getPrice(), updatedPlant.getPrice(), 0.01);
        Assert.assertEquals(plantRequest.getQuantity(), updatedPlant.getQuantity());
    }

    @Given("I provide invalid update data with price {int}")
    public void iProvideInvalidUpdateDataWithPrice(int price) {
        // Create a partial object or just a map for invalid data
        plantRequest = new Plant("Invalid", price, 5);
        request.body(plantRequest);
    }

    @Then("a validation error should be returned")
    public void aValidationErrorShouldBeReturned() {
        // Assert response contains validation error details
        Assert.assertTrue(
                response.getBody().asString().toLowerCase().contains("error") || response.getStatusCode() >= 400);
    }

    @Given("I have a plant id {int} to delete")
    public void iHaveAPlantIdToDelete(int id) {
        this.plantId = id;
    }

    @When("I send a DELETE request to delete the plant")
    public void iSendADELETERequestToDeleteThePlant() {
        response = request.delete("/plants/" + plantId);
    }

    @Given("I have a category id {int}")
    public void iHaveACategoryId(int id) {
        this.categoryId = id;
    }

    @Then("the response should be a list of plants")
    public void theResponseShouldBeAListOfPlants() {
        List<Plant> plants = response.jsonPath().getList("", Plant.class);
        Assert.assertNotNull(plants);
        // Assert.assertFalse(plants.isEmpty()); // Optional, depending on data
    }

    @Given("I provide plant data name {string}, price {double}, quantity {int}")
    public void iProvidePlantData(String name, double price, int quantity) {
        plantRequest = new Plant(name, price, quantity);
        request.body(plantRequest);
    }

    @When("I send a POST request to create the plant in category {int}")
    public void iSendAPOSTRequestToCreateThePlantInCategory(int catId) {
        plantRequest.setCategoryId(catId);
        request.body(plantRequest);
        response = request.post("/plants");
    }

    @Then("a new plant should be created")
    public void aNewPlantShouldBeCreated() {
        // Verify creation, maybe check ID presence
        Assert.assertNotNull(response.jsonPath().get("id"));
        Assert.assertEquals(plantRequest.getName(), response.jsonPath().getString("name"));
    }

    @Given("I provide invalid plant data with quantity {int}")
    public void iProvideInvalidPlantDataWithQuantity(int quantity) {
        plantRequest = new Plant("Invalid", 10.0, quantity);
        request.body(plantRequest);
    }
}
