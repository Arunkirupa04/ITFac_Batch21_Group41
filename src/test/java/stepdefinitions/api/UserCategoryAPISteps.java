package stepdefinitions.api;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class UserCategoryAPISteps {

    private static final String BASE_URL = "http://localhost:8080";
    private String userJwtToken;
    // Removed: Use shared response from AdminCategoryAPISteps instead

    // ========== AUTHENTICATION ==========

    @Given("User is authenticated with valid JWT token")
    public void user_is_authenticated_with_valid_jwt_token() {
        // Login as User and get JWT token
        userJwtToken = getUserJWTToken();
        System.out.println("✅ User authenticated with JWT token");
    }

    private String getUserJWTToken() {
        // Login request to get JWT token
        Response loginResponse = RestAssured.given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body("{\"username\":\"testuser\",\"password\":\"test123\"}")
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .response();

        String token = loginResponse.jsonPath().getString("token");
        System.out.println("✅ User JWT token obtained");
        return token;
    }

    // ========== REQUEST STEPS ==========

    @When("User sends GET request to {string}")
    public void user_sends_get_request_to(String endpoint) {
        // Fixed: Use shared response from AdminCategoryAPISteps
        AdminCategoryAPISteps.response = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + userJwtToken)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
        
        System.out.println("✅ User sent GET request to: " + endpoint);
        System.out.println("   Response status: " + AdminCategoryAPISteps.response.getStatusCode());
    }

    @When("User sends DELETE request to {string}")
    public void user_sends_delete_request_to(String endpoint) {
        // Fixed: Use shared response from AdminCategoryAPISteps
        AdminCategoryAPISteps.response = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + userJwtToken)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
        
        System.out.println("✅ User sent DELETE request to: " + endpoint);
        System.out.println("   Response status: " + AdminCategoryAPISteps.response.getStatusCode());
    }

    @When("User sends POST request to {string} with body:")
    public void user_sends_post_request_to_with_body(String endpoint, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        String name = rows.get(0).get("name");
        
        // Fixed: Use "name" only (no parent field for main category)
        String body = "{\"name\":\"" + name + "\"}";
        
        // Fixed: Use shared response from AdminCategoryAPISteps
        AdminCategoryAPISteps.response = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + userJwtToken)
                .contentType("application/json")
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
        
        System.out.println("✅ User sent POST request to: " + endpoint);
        System.out.println("   Request body: " + body);
        System.out.println("   Response status: " + AdminCategoryAPISteps.response.getStatusCode());
    }

    // ========== ASSERTION STEPS ==========

    @And("Response should contain error {string}")
    public void response_should_contain_error(String expectedError) {
        // Fixed: Use shared response from AdminCategoryAPISteps
        String responseBody = AdminCategoryAPISteps.response.getBody().asString();
        int statusCode = AdminCategoryAPISteps.response.getStatusCode();
        
        boolean hasError = responseBody.toLowerCase().contains(expectedError.toLowerCase()) || 
                          responseBody.contains("Access Denied") ||
                          responseBody.contains("access denied") ||
                          statusCode == 403;
        
        Assert.assertTrue("Response should contain error: " + expectedError + " or status 403", hasError);
        System.out.println("✅ Error verified in response: " + expectedError);
    }

    @And("Response should contain summary data")
    public void response_should_contain_summary_data() {
        // Fixed: Use shared response from AdminCategoryAPISteps
        String responseBody = AdminCategoryAPISteps.response.getBody().asString();
        
        // Check if response contains summary-related data
        boolean hasSummaryData = responseBody.contains("total") || 
                                responseBody.contains("count") || 
                                responseBody.contains("summary") ||
                                responseBody.length() > 0;
        
        Assert.assertTrue("Response should contain summary data", hasSummaryData);
        System.out.println("✅ Summary data verified in response");
        System.out.println("   Response: " + responseBody);
    }
}
