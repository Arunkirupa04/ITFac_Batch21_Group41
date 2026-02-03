package stepdefinitions.api;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import static org.hamcrest.Matchers.*;

public class AdminAPISteps {
    private static String adminToken;

    @Given("admin credentials username {string} and password {string}")
    public void setAdminCredentials(String user, String pass) {
        BaseSteps.request = RestAssured.given()
                .contentType("application/json")
                .body("{\"username\":\"" + user + "\", \"password\":\"" + pass + "\"}");
    }

    @When("admin sends POST request to {string}")
    public void adminPostLogin(String endpoint) {
        BaseSteps.response = BaseSteps.request.post(endpoint);
    }

    @Then("JWT token should be generated")
    public void jwt_token_should_be_generated() {
        BaseSteps.response.then().body("token", notNullValue());
        adminToken = BaseSteps.response.jsonPath().getString("token");
    }

    @Given("admin is logged in with JWT")
    public void admin_is_logged_in_with_jwt() {
        BaseSteps.request = RestAssured.given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType("application/json");
    }

    @When("admin sends GET request to {string}")
    public void admin_sends_get_request_to(String endpoint) {
        BaseSteps.response = BaseSteps.request.get(endpoint);
    }

    @Given("admin sends malformed JSON body")
    public void admin_sends_malformed_json_body() {
        BaseSteps.request = RestAssured.given()
                .contentType("application/json")
                .body("{\"username\": \"admin\", \"password\": ");
    }
}