package stepdefinitions.api;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import static org.hamcrest.Matchers.*;

public class UserAPISteps {
    private static String userToken;

    @Given("user credentials username {string} and password {string}")
    public void user_credentials_username_and_password(String user, String pass) {
        BaseSteps.request = RestAssured.given()
                .contentType("application/json")
                .body("{\"username\":\"" + user + "\", \"password\":\"" + pass + "\"}");
    }

    @When("user sends POST request to {string}")
    public void user_sends_post_request_to(String endpoint) {
        BaseSteps.response = BaseSteps.request.post(endpoint);
    }

    @Then("user response code should be {int}")
    public void user_response_code_should_be(Integer code) {
        BaseSteps.response.then().statusCode(code);
    }

    @Given("user is logged in with JWT")
    public void user_is_logged_in_with_jwt() {
        BaseSteps.request = RestAssured.given()
                .header("Authorization", "Bearer " + userToken)
                .contentType("application/json");
    }

    @When("user sends GET request to {string}")
    public void user_sends_get_request_to(String endpoint) {
        BaseSteps.response = BaseSteps.request.get(endpoint);
    }

    @When("user sends GET request without auth to {string}")
    public void user_sends_get_request_without_auth_to(String endpoint) {
        BaseSteps.response = RestAssured.get(endpoint);
    }

    @When("user sends PUT request to {string} with body:")
    public void user_sends_put_request_to_with_body(String endpoint, String body) {
        BaseSteps.response = BaseSteps.request.body(body).put(endpoint);
    }

    @And("user JWT token should be generated")
    public void captureUserToken() {
        BaseSteps.response.then().body("token", notNullValue());
        userToken = BaseSteps.response.jsonPath().getString("token");
    }
}