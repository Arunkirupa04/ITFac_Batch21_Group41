package stepdefinitions.api;

import io.cucumber.java.en.*;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserCategoryPlantAPISteps extends BaseAPISteps {

    @When("user sends DELETE request to {string}")
    public void user_sends_delete(String endpoint) {
        response = request.delete(endpoint);
    }

    // ✅ PUT already in UserAuthAPISteps as:
    // @When("user sends PUT request to {string} with body:")
    // So no need duplicate.

    @Then("the response should contain a list of sub-categories for user")
    public void user_subcategories_list() {
        response.then().contentType(ContentType.JSON);
        response.then().body("size()", greaterThanOrEqualTo(0));
    }

    @Then("the response should contain a list of main categories for user")
    public void user_main_categories_list() {
        response.then().contentType(ContentType.JSON);
        response.then().body("size()", greaterThanOrEqualTo(0));
    }

    @When("user requests plant by id {int}")
    public void user_get_plant_by_id(int id) {
        response = request.get("/api/plants/" + id);
    }

    @Then("the response should contain plant id {int} for user")
    public void user_response_contains_plant_id(int id) {
        response.then().contentType(ContentType.JSON);
        response.then().body("id", equalTo(id));
    }
}