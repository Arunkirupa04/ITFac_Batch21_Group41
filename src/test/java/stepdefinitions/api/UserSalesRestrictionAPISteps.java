package stepdefinitions.api;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import static io.restassured.RestAssured.given;

public class UserSalesRestrictionAPISteps extends BaseAPISteps {

    /** Build a new request with fresh user login – never use static request so we never send as admin. */
    private RequestSpecification newUserRequest() {
        Response loginResp = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body("{\"username\":\"testuser\",\"password\":\"test123\"}")
                .post("/api/auth/login");
        Assert.assertEquals("User login failed", 200, loginResp.getStatusCode());
        String token = loginResp.jsonPath().getString("token");
        Assert.assertNotNull("User token missing", token);
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON);
    }

    /** Ensure request uses user JWT (for steps that only read response; When steps use newUserRequest()). */
    private void ensureUserRequest() {
        if (userToken == null) {
            user_is_authenticated_via_api();
        }
        request = given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + userToken)
                .contentType(ContentType.JSON);
    }

    @When("user sends a POST request to {string} with quantity {int}")
    public void user_sends_post_with_quantity(String endpoint, int quantity) {
        RequestSpecification userReq = newUserRequest();
        if (foundPlantId != 0 && endpoint.contains("/api/sales/plant/")) {
            endpoint = endpoint.replaceFirst("/api/sales/plant/\\d+", "/api/sales/plant/" + foundPlantId);
        }
        response = userReq.queryParam("quantity", quantity).post(endpoint);
    }

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
        RequestSpecification userReq = newUserRequest();
        if (foundPlantId != 0 && endpoint.contains("/api/sales/plant/")) {
            endpoint = endpoint.replaceFirst("/api/sales/plant/\\d+", "/api/sales/plant/" + foundPlantId);
        }
        if (body.contains("\"quantity\":")) {
            String qtyStr = body.replaceAll("(?s).*\"quantity\":\\s*(\\d+).*", "$1");
            response = userReq.queryParam("quantity", Integer.parseInt(qtyStr)).post(endpoint);
        } else {
            response = userReq.body(body).post(endpoint);
        }
    }

    @When("user sends a GET request to {string}")
    public void user_sends_get(String endpoint) {
        RequestSpecification userReq = newUserRequest();
        if (dynamicSaleId != null && endpoint.equals("/api/sales/1")) {
            endpoint = "/api/sales/" + dynamicSaleId;
        }
        response = userReq.get(endpoint);
    }

    @When("user sends a GET request to retrieve the sale")
    public void user_retrieve_sale() {
        RequestSpecification userReq = newUserRequest();
        response = userReq.get("/api/sales/" + createdSaleId);
    }

    @When("user sends a DELETE request to remove the sale")
    public void user_delete_sale() {
        RequestSpecification userReq = newUserRequest();
        response = userReq.delete("/api/sales/" + createdSaleId);
    }

    @When("user sends a DELETE request to {string}")
    public void user_sends_delete(String endpoint) {
        RequestSpecification userReq = newUserRequest();
        if (dynamicSaleId != null && endpoint.equals("/api/sales/1")) {
            endpoint = "/api/sales/" + dynamicSaleId;
        }
        response = userReq.delete(endpoint);
    }
}
