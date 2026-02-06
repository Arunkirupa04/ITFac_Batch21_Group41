package stepdefinitions.api;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import org.junit.Assert;
import static io.restassured.RestAssured.given;

public class UserSalesRestrictionAPISteps extends BaseAPISteps {

    @When("user sends a POST request to {string} with quantity {int}")
    public void user_sends_post_with_quantity(String endpoint, int quantity) {
        if (foundPlantId != 0 && endpoint.contains("/api/sales/plant/")) {
            endpoint = endpoint.replaceFirst("/api/sales/plant/\\d+", "/api/sales/plant/" + foundPlantId);
        }
        response = request.queryParam("quantity", quantity).post(endpoint);
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
        if (foundPlantId != 0 && endpoint.contains("/api/sales/plant/")) {
            endpoint = endpoint.replaceFirst("/api/sales/plant/\\d+", "/api/sales/plant/" + foundPlantId);
        }
        if (body.contains("\"quantity\":")) {
            String qtyStr = body.replaceAll("(?s).*\"quantity\":\\s*(\\d+).*", "$1");
            response = request.queryParam("quantity", Integer.parseInt(qtyStr)).post(endpoint);
        } else {
            response = request.body(body).post(endpoint);
        }
    }

    @When("user sends a GET request to {string}")
    public void user_sends_get(String endpoint) {
        response = request.get(endpoint);
    }

    @When("user sends a GET request to retrieve the sale")
    public void user_retrieve_sale() {
        response = request.get("/api/sales/" + createdSaleId);
    }

    @When("user sends a DELETE request to remove the sale")
    public void user_delete_sale() {
        response = request.delete("/api/sales/" + createdSaleId);
    }

    @When("user sends a DELETE request to {string}")
    public void user_sends_delete(String endpoint) {
        response = request.delete(endpoint);
    }
}
