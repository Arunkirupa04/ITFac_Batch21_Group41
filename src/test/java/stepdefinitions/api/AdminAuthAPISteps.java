package stepdefinitions.api;

import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
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
    @Given("admin is authenticated via API")
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

    @Given("a category exists for admin retrieval")
    public void a_category_exists_for_admin_retrieval() {
        // Use short name (API may enforce 3–10 chars, same as AdminCategoryAPISteps); fallback to first existing if create returns 400
        String name = "Adm" + (System.currentTimeMillis() % 10000);
        Response createRes = request.body("{\"name\":\"" + name + "\"}").post("/api/categories");
        if (createRes.getStatusCode() == 201 || createRes.getStatusCode() == 200) {
            categoryIdForAdminRetrieval = createRes.jsonPath().getInt("id");
        } else {
            // Fallback: use first category from GET /api/categories (don't rely on id=1)
            Response listRes = request.get("/api/categories");
            if (listRes.getStatusCode() == 200) {
                java.util.List<Integer> ids = listRes.jsonPath().getList("id");
                if (ids != null && !ids.isEmpty()) {
                    categoryIdForAdminRetrieval = ids.get(0);
                } else {
                    throw new AssertionError("Failed to create category and no categories from GET /api/categories. Create status: " + createRes.getStatusCode());
                }
            } else {
                throw new AssertionError("Failed to create category for TC_API_ADMIN_07: " + createRes.getStatusCode());
            }
        }
    }

    @When("admin sends GET request to retrieve that category")
    public void admin_sends_get_request_to_retrieve_that_category() {
        response = request.get("/api/categories/" + categoryIdForAdminRetrieval);
    }

    @And("response JSON should contain the retrieved category id")
    public void response_json_should_contain_retrieved_category_id() {
        response.then().body("id", equalTo(categoryIdForAdminRetrieval));
    }
}
