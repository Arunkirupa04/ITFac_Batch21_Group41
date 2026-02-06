package stepdefinitions.api;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserAuthAPISteps extends BaseAPISteps {

    @Given("user credentials username {string} and password {string}")
    public void user_credentials(String username, String password) {
        request = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}");
    }

    @When("user sends POST request to {string}")
    public void user_sends_post_login(String endpoint) {
        response = request.post(endpoint);
    }

    @Then("user response code should be {int}")
    public void user_response_code(int code) {
        response.then().statusCode(code);
    }

    @And("user JWT token should be generated")
    public void user_jwt_generated() {
        response.then().body("token", notNullValue());
        userToken = response.jsonPath().getString("token");
    }

    @Given("user is logged in with JWT")
    public void user_logged_in_with_jwt() {
        if (userToken == null) {
            String loginBody = "{\"username\": \"testuser\", \"password\": \"test123\"}";
            userToken = given().baseUri(BASE_URL).contentType(ContentType.JSON).body(loginBody)
                    .post("/api/auth/login").jsonPath().getString("token");
        }
        request = given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + userToken)
                .contentType(ContentType.JSON);
    }

    @When("user sends GET request to {string}")
    public void user_sends_get(String endpoint) {
        response = request.get(endpoint);
    }

    @When("user sends GET request without auth to {string}")
    public void user_sends_get_no_auth(String endpoint) {
        response = RestAssured.get(BASE_URL + endpoint);
    }

    @When("user sends PUT request to {string} with body:")
    public void user_sends_put(String endpoint, String body) {
        response = request.body(body).put(endpoint);
    }
}
