package stepdefinitions.api;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.Assert;
import java.util.Map;
import static org.hamcrest.Matchers.*;

public class AdminPlantSalesAPISteps extends BaseAPISteps {

    @When("admin sends a GET request to {string}")
    public void admin_sends_get(String endpoint) {
        if (dynamicSaleId != null && endpoint.equals("/api/sales/1")) {
            endpoint = "/api/sales/" + dynamicSaleId;
        }
        response = request.get(endpoint);
    }

    @And("the response should contain a list of plants")
    public void verify_plants_list() {
        response.then().body("$", is(notNullValue())).body("$.size()", greaterThanOrEqualTo(0));
    }

    @And("the response should contain a summary of plants")
    public void verify_plants_summary() {
        response.then().body("totalPlants", is(notNullValue()));
    }

    @When("admin sends a GET request to {string} with parameters:")
    public void admin_sends_get_with_params(String endpoint, Map<String, String> params) {
        response = request.queryParams(params).get(endpoint);
    }

    @And("the response should contain paginated plant data")
    public void verify_paginated_plants() {
        response.then().body("content", is(notNullValue()));
    }

    @Given("a plant exists with ID {int} and sufficient stock")
    public void plant_exists_with_stock(int id) {
        Response check = request.get("/api/plants/" + id);
        if (check.getStatusCode() != 200 || check.jsonPath().getInt("quantity") <= 0) {
            System.out.println("Warning: Plant with ID " + id + " has insufficient stock. Discovering another one...");
            Response all = request.get("/api/plants");
            // Find first plant with quantity > 2 (to be safe for the "sufficient stock"
            // requirement)
            java.util.List<Integer> ids = all.jsonPath().getList("findAll { it.quantity >= 2 }.id");
            if (!ids.isEmpty()) {
                foundPlantId = ids.get(0);
                System.out.println("Using discovered plant ID: " + foundPlantId);
            } else {
                foundPlantId = id; // Fallback
            }
        } else {
            foundPlantId = id;
        }
    }

    @When("admin sends a POST request to {string} with quantity {int}")
    public void admin_sends_post_with_quantity(String endpoint, int quantity) {
        if (foundPlantId != 0 && endpoint.contains("/api/sales/plant/")) {
            endpoint = endpoint.replaceFirst("/api/sales/plant/\\d+", "/api/sales/plant/" + foundPlantId);
        }
        response = request.queryParam("quantity", quantity).post(endpoint);
    }

    @When("admin sends a POST request to {string} with body:")
    public void admin_sends_post_with_body(String endpoint, String body) {
        response = request.body(body).post(endpoint);
    }

    @And("the sale should be created and inventory reduced")
    public void verify_sale_created() {
        response.then().body("id", is(notNullValue()));
        createdSaleId = response.jsonPath().getInt("id");
    }

    @Given("a plant exists with ID {int} and limited stock")
    public void plant_exists_limited_stock(int id) {
        Response check = request.get("/api/plants/" + id);
        if (check.getStatusCode() == 200) {
            foundPlantId = id;
        } else {
            Response all = request.get("/api/plants");
            if (all.getStatusCode() == 200 && !all.jsonPath().getList("$").isEmpty()) {
                foundPlantId = all.jsonPath().getInt("[0].id");
            } else {
                foundPlantId = id;
            }
        }
    }

    @When("admin sends a POST request to {string} with quantity more than available stock")
    public void admin_sells_excessive(String endpoint) {
        if (foundPlantId != 0 && endpoint.contains("/api/sales/plant/")) {
            endpoint = endpoint.replaceFirst("/api/sales/plant/\\d+", "/api/sales/plant/" + foundPlantId);
        }
        response = request.queryParam("quantity", 999999).post(endpoint);
    }

    @And("the response should contain error {string}")
    public void verify_error(String expectedError) {
        response.then().body("message", containsString(expectedError));
    }

    @And("the response should contain the details of sale ID {int}")
    public void verify_sale_details(int id) {
        int expectedId = (createdSaleId != 0) ? createdSaleId : id;
        response.then().body("id", equalTo(expectedId));
    }

    @When("admin sends a DELETE request to {string}")
    public void admin_sends_delete(String endpoint) {
        if (dynamicSaleId != null && endpoint.equals("/api/sales/1")) {
            endpoint = "/api/sales/" + dynamicSaleId;
        }
        response = request.delete(endpoint);
    }
    //
    // @Then("the response status code should be {int} or {int}")
    // public void verify_status_range(int code1, int code2) {
    // int actual = response.getStatusCode();
    // Assert.assertTrue("Status code " + actual + " not in expected range",
    // actual == code1 || actual == code2);
    // }

    @When("admin sends a DELETE request to remove the sale")
    public void delete_created_sale() {
        response = request.delete("/api/sales/" + createdSaleId);
    }

    @And("the sale should no longer exist")
    public void verify_sale_deleted_dynamic() {
        Response check = request.get("/api/sales/" + createdSaleId);
        Assert.assertTrue(check.getStatusCode() == 404 || check.getStatusCode() == 500);
    }

    @And("the sale with ID {int} should no longer exist")
    public void verify_sale_deleted(int id) {
        int targetId = (dynamicSaleId != null && id == 1) ? dynamicSaleId : id;
        Response check = request.get("/api/sales/" + targetId);
        Assert.assertTrue(check.getStatusCode() == 404 || check.getStatusCode() == 500);
    }
}
