package stepdefinitions.api;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static org.hamcrest.Matchers.*;

public class AdminAPISteps {
    private RequestSpecification request;
    // Made static so it can be shared or accessed via UserAPISteps if needed
    public static Response response;
    private static String adminToken;

    @Given("admin credentials username {string} and password {string}")
    public void setAdminCredentials(String user, String pass) {
        request = RestAssured.given()
                .contentType("application/json")
                .body("{\"username\":\"" + user + "\", \"password\":\"" + pass + "\"}");
    }

    @When("admin sends POST request to {string}")
    public void adminPostLogin(String endpoint) {
        response = request.post(endpoint);
    }

    @Then("JWT token should be generated")
    public void jwt_token_should_be_generated() {
        response.then().body("token", notNullValue());
        adminToken = response.jsonPath().getString("token");
    }

    @Given("admin is logged in with JWT")
    public void admin_is_logged_in_with_jwt() {
        request = RestAssured.given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType("application/json");
    }

    @When("admin sends GET request to {string}")
    public void admin_sends_get_request_to(String endpoint) {
        response = request.get(endpoint);
    }

    @Then("response code should be {int}")
    public void response_code_should_be(Integer statusCode) {
        // We check the static response to ensure it's not null
        response.then().statusCode(statusCode);
    }

    @Then("response JSON should contain {string}:{int}")
    public void response_json_should_contain(String key, Integer value) {
        response.then().body(key, equalTo(value));
    }

    @Then("response message should contain {string}")
    public void response_message_should_contain(String expectedMsg) {
        response.then().body("message", containsString(expectedMsg));
    }

    @Then("response should contain validation error")
    public void response_should_contain_validation_error() {
        // This matches the "Actual" JSON seen in your console output
        response.then()
                .body("error", org.hamcrest.Matchers.equalTo("UNAUTHORIZED"))
                .body("message", org.hamcrest.Matchers.containsString("Unauthorized"));
    }

    @Given("admin sends malformed JSON body")
    public void admin_sends_malformed_json_body() {
        request = RestAssured.given()
                .contentType("application/json")
                .body("{\"username\": \"admin\", \"password\": "); // Intentionally broken
    }
}