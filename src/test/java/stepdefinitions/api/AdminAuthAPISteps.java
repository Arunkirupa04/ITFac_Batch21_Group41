package stepdefinitions.api;

import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AdminAuthAPISteps extends BaseAPISteps {

    @Given("admin credentials username {string} and password {string}")
    public void admin_credentials(String username, String password) {
        request = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}");
    }

    @When("admin sends POST request to {string}")
    public void admin_sends_post_login(String endpoint) {
        response = request.post(endpoint);
    }

    @And("JWT token should be generated")
    public void jwt_token_generated() {
        response.then().body("token", notNullValue());
        adminToken = response.jsonPath().getString("token");
    }

    @And("response message should contain {string}")
    public void response_message_contains(String expected) {
        response.then().body("message", containsString(expected));
    }

    @And("response should contain validation error")
    public void response_validation_error() {
        // Matches the behavior where empty fields lead to unauthorized/validation
        // errors
        response.then().body("error", is(notNullValue()));
    }

    @Given("admin sends malformed JSON body")
    public void admin_sends_malformed_json() {
        request = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body("{\"username\": \"admin\", \"password\": "); // Broken JSON
    }

    @Given("admin is logged in with JWT")
    public void admin_logged_in_with_jwt() {
        if (adminToken == null) {
            // Self-heal/Bootstrap if token is missing
            String loginBody = "{\"username\": \"admin\", \"password\": \"admin123\"}";
            adminToken = given().baseUri(BASE_URL).contentType(ContentType.JSON).body(loginBody)
                    .post("/api/auth/login").jsonPath().getString("token");
        }
        request = given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON);
    }

    @When("admin sends GET request to {string}")
    public void admin_sends_get_request(String endpoint) {
        response = request.get(endpoint);
    }
}
