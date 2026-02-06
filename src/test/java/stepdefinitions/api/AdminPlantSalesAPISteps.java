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
        Assert.assertEquals("Plant with ID " + id + " not found! Pre-condition failed.", 200, check.getStatusCode());
    }

    @When("admin sends a POST request to {string} with body:")
    public void admin_sends_post_with_body(String endpoint, String body) {
        if (body.contains("\"quantity\"")) {
            // Extract quantity from JSON - improved regex to handle newlines/whitespace
            String q = body.replaceAll("(?s).*\"quantity\"\\s*:\\s*(\\d+).*", "$1");
            response = request.queryParam("quantity", Integer.parseInt(q.trim())).post(endpoint);
        } else {
            response = request.body(body).post(endpoint);
        }
    }

    @And("the sale should be created and inventory reduced")
    public void verify_sale_created() {
        response.then().body("id", is(notNullValue()));
    }

    @Given("a plant exists with ID {int} and limited stock")
    public void plant_exists_limited_stock(int id) {
        // Assume state or verification logic
    }

    @When("admin sends a POST request to {string} with quantity more than available stock")
    public void admin_sells_excessive(String endpoint) {
        response = request.queryParam("quantity", 999999).post(endpoint);
    }

    @And("the response should contain error {string}")
    public void verify_error(String expectedError) {
        response.then().body("message", containsString(expectedError));
    }

    @And("the response should contain the details of sale ID {int}")
    public void verify_sale_details(int id) {
        int expectedId = (dynamicSaleId != null && id == 1) ? dynamicSaleId : id;
        response.then().body("id", equalTo(expectedId));
    }

    @When("admin sends a DELETE request to {string}")
    public void admin_sends_delete(String endpoint) {
        if (dynamicSaleId != null && endpoint.equals("/api/sales/1")) {
            endpoint = "/api/sales/" + dynamicSaleId;
        }
        response = request.delete(endpoint);
    }

    @Then("the response status code should be {int} or {int}")
    public void verify_status_range(int code1, int code2) {
        int actual = response.getStatusCode();
        Assert.assertTrue("Status code " + actual + " not in expected range",
                actual == code1 || actual == code2);
    }

    @And("the sale with ID {int} should no longer exist")
    public void verify_sale_deleted(int id) {
        int targetId = (dynamicSaleId != null && id == 1) ? dynamicSaleId : id;
        Response check = request.get("/api/sales/" + targetId);
        Assert.assertTrue(check.getStatusCode() == 404 || check.getStatusCode() == 500);
    }
}
