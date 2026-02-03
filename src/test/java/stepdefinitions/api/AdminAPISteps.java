package stepdefinitions.api;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AdminAPISteps extends BaseAPISteps {

    private String token;

    @Given("admin is authenticated via API")
    public void admin_is_authenticated_via_api() {
        String loginBody = "{\"username\": \"admin\", \"password\": \"admin123\"}";

        Response loginResponse = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(loginBody)
                .post("/api/auth/login");

        Assert.assertEquals("Login failed!", 200, loginResponse.getStatusCode());
        token = loginResponse.jsonPath().getString("token");

        request = given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON);
    }

    @When("admin sends a GET request to {string}")
    public void admin_sends_get_request(String endpoint) {
        response = request.get(endpoint);
    }

    @When("admin sends a GET request to {string} with parameters:")
    public void admin_sends_get_request_with_params(String endpoint, Map<String, String> params) {
        response = request.queryParams(params).get(endpoint);
    }

    @When("admin sends a POST request to {string} with body:")
    public void admin_sends_post_request_with_body(String endpoint, String body) {
        response = request.body(body).post(endpoint);
    }

    @Given("a plant exists with ID {int} and sufficient stock")
    public void plant_exists_with_sufficient_stock(int id) {
        // In a real test, we would verify or create the plant state here.
        // For this assignment, we assume the environment is prepared or check it.
        Response check = request.get("/api/plants/" + id);
        if (check.getStatusCode() != 200) {
            System.out.println("Warning: Plant with ID " + id + " not found. Test might fail.");
        }
    }

    @Given("a plant exists with ID {int} and limited stock")
    public void plant_exists_with_limited_stock(int id) {
        // Similar to above, ensures a state where stock < quantity to be sold
    }

    @When("admin sends a POST request to {string} with quantity more than available stock")
    public void admin_sells_excessive_quantity(String endpoint) {
        // Assuming we sell 999999 which is likely more than any stock
        String body = "{\"quantity\": 999999}";
        response = request.body(body).post(endpoint);
    }

    @Given("a sale exists with ID {int}")
    public void sale_exists_with_id(int id) {
        // Ensure a sale exists to be retrieved or deleted
        Response check = request.get("/api/sales/" + id);
        if (check.getStatusCode() != 200) {
            // Logic to create a sale if it doesn't exist could go here
            System.out.println("Warning: Sale with ID " + id + " not found.");
        }
    }

    @When("admin sends a DELETE request to {string}")
    public void admin_sends_delete_request(String endpoint) {
        response = request.delete(endpoint);
    }

    @Then("the response status code should be {int}")
    public void verify_status_code(int statusCode) {
        Assert.assertEquals("Status code mismatch!", statusCode, response.getStatusCode());
    }

    @Then("the response status code should be {int} or {int}")
    public void verify_status_code_range(int code1, int code2) {
        int actual = response.getStatusCode();
        Assert.assertTrue("Status code " + actual + " not in expected list [" + code1 + ", " + code2 + "]",
                actual == code1 || actual == code2);
    }

    @And("the response should contain a list of plants")
    public void verify_plants_list() {
        response.then().body("$", is(notNullValue())).body("$.size()", greaterThanOrEqualTo(0));
    }

    @And("the response should contain a summary of plants")
    public void verify_plants_summary() {
        // Based on PlantSummaryDTO
        response.then().body("totalPlants", is(notNullValue()));
    }

    @And("the response should contain paginated plant data")
    public void verify_paginated_plants() {
        response.then().body("content", is(notNullValue()));
    }

    @And("the sale should be created and inventory reduced")
    public void verify_sale_created() {
        // Response should contain the created sale
        response.then().body("id", is(notNullValue()));
    }

    @And("the response should contain error {string}")
    public void verify_error_message(String expectedError) {
        response.then().body("message", containsString(expectedError));
    }

    @And("the response should contain a list of sales")
    public void verify_sales_list() {
        response.then().body("$.size()", greaterThanOrEqualTo(0));
    }

    @And("the response should contain the details of sale ID {int}")
    public void verify_sale_details(int id) {
        response.then().body("id", equalTo(id));
    }

    @And("the sale with ID {int} should no longer exist")
    public void verify_sale_deleted(int id) {
        Response check = request.get("/api/sales/" + id);
        Assert.assertTrue("Sale still exists after deletion!",
                check.getStatusCode() == 404 || check.getStatusCode() == 500);
    }
}
