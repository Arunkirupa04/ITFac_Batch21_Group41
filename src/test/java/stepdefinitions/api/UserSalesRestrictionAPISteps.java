package stepdefinitions.api;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import org.junit.Assert;
import static io.restassured.RestAssured.given;

public class UserSalesRestrictionAPISteps extends BaseAPISteps {

    @Given("user is authenticated via API")
    public void user_is_authenticated_via_api() {
        String loginBody = "{\"username\": \"testuser\", \"password\": \"test123\"}";
        Response loginResponse = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(loginBody)
                .post("/api/auth/login");

        Assert.assertEquals("Login failed!", 200, loginResponse.getStatusCode());
        userToken = loginResponse.jsonPath().getString("token");

        request = given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + userToken)
                .contentType(ContentType.JSON);
    }

    @When("user sends a POST request to {string} with body:")
    public void user_sends_post_with_body(String endpoint, String body) {
        response = request.body(body).post(endpoint);
    }

    @When("user sends a GET request to {string}")
    public void user_sends_get(String endpoint) {
        response = request.get(endpoint);
    }

    @When("user sends a DELETE request to {string}")
    public void user_sends_delete(String endpoint) {
        response = request.delete(endpoint);
    }
}
