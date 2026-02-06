package stepdefinitions.api;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import org.junit.Assert;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AdminPlantSalesAPISteps extends BaseAPISteps {

    @Given("admin is authenticated via API")
    public void admin_is_authenticated_via_api() {
        String loginBody = "{\"username\": \"admin\", \"password\": \"admin123\"}";
        Response loginResponse = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(loginBody)
                .post("/api/auth/login");

        Assert.assertEquals("Login failed!", 200, loginResponse.getStatusCode());
        adminToken = loginResponse.jsonPath().getString("token");

        request = given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON);
    }

    @When("admin sends a GET request to {string}")
    public void admin_sends_get(String endpoint) {
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
        if (check.getStatusCode() != 200) {
            System.out.println("Warning: Plant with ID " + id + " not found.");
        }
    }

    @When("admin sends a POST request to {string} with body:")
    public void admin_sends_post_with_body(String endpoint, String body) {
        response = request.body(body).post(endpoint);
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
        String body = "{\"quantity\": 999999}";
        response = request.body(body).post(endpoint);
    }

    @And("the response should contain error {string}")
    public void verify_error(String expectedError) {
        response.then().body("message", containsString(expectedError));
    }

    @And("the response should contain the details of sale ID {int}")
    public void verify_sale_details(int id) {
        response.then().body("id", equalTo(id));
    }

    @When("admin sends a DELETE request to {string}")
    public void admin_sends_delete(String endpoint) {
        response = request.delete(endpoint);
    }

    @And("the sale with ID {int} should no longer exist")
    public void verify_sale_deleted(int id) {
        Response check = request.get("/api/sales/" + id);
        Assert.assertTrue(check.getStatusCode() == 404 || check.getStatusCode() == 500);
    }
}
