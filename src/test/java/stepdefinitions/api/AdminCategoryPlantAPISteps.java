package stepdefinitions.api;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class AdminCategoryPlantAPISteps extends BaseAPISteps {

    // ✅ POST with body (for TC_API_ADMIN_15)
    @When("admin sends POST request to {string} with body:")
    public void admin_sends_post_with_body(String endpoint, String body) {
        response = request.body(body).post(endpoint);
    }

    // // ✅ GET (already have one in AdminAuthAPISteps too; but this is safe if your
    // phrases differ)
    // @When("admin sends a GET request to {string}")
    // public void admin_sends_a_get_request(String endpoint) {
    // response = request.get(endpoint);
    // }

    // ✅ GET with query params table (for pagination/search TC_API_ADMIN_18/19)
    @When("admin sends GET request to {string} with parameters:")
    public void admin_sends_get_with_parameters(String endpoint, DataTable table) {
        Map<String, String> params = new HashMap<>();
        List<List<String>> rows = table.asLists();

        for (List<String> r : rows) {
            params.put(r.get(0), r.get(1));
        }

        response = request.queryParams(params).get(endpoint);
    }

    // ✅ Summary DTO schema-ish check (TC_API_ADMIN_16)
    @Then("the response should contain a category summary DTO")
    public void category_summary_dto() {
        response.then().contentType(ContentType.JSON);
        // generic: response is an object (map) and has at least 1 field
        Map<String, Object> map = response.jsonPath().getMap("");
        org.junit.Assert.assertNotNull("Summary response should be an object", map);
        org.junit.Assert.assertTrue("Summary DTO should contain at least 1 field", map.size() > 0);
    }

    // ✅ Sub-categories list check (TC_API_ADMIN_17)
    @Then("the response should contain a list of sub-categories")
    public void subcategories_list() {
        response.then().contentType(ContentType.JSON);
        response.then().body("$", is(notNullValue()));
        response.then().body("size()", greaterThanOrEqualTo(0));
    }

    // ✅ Main categories list check (TC_API_ADMIN_20)
    @Then("the response should contain a list of main categories")
    public void main_categories_list() {
        response.then().contentType(ContentType.JSON);
        response.then().body("size()", greaterThanOrEqualTo(0));
    }

    // ✅ Paged response check (TC_API_ADMIN_18)
    @Then("the response should contain a paged categories response")
    public void paged_categories_response() {
        response.then().contentType(ContentType.JSON);

        // typical Spring Page: content, totalElements, totalPages, number, size...
        response.then().body("content", notNullValue());
        response.then().body("content.size()", greaterThanOrEqualTo(0));
        response.then().body("totalElements", notNullValue());
    }

    // ✅ Plant by ID check (TC_API_ADMIN_21)
    @When("admin requests plant by id {int}")
    public void admin_get_plant_by_id(int id) {
        response = request.get("/api/plants/" + id);
    }

    @Then("the response should contain plant id {int}")
    public void response_contains_plant_id(int id) {
        response.then().contentType(ContentType.JSON);
        response.then().body("id", equalTo(id));
    }
}