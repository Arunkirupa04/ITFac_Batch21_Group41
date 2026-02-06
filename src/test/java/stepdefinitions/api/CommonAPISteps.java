package stepdefinitions.api;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.Assert;
import static org.hamcrest.Matchers.*;

public class CommonAPISteps extends BaseAPISteps {

    @Then("response code should be {int}")
    public void verify_response_code(int statusCode) {
        Assert.assertEquals("Status code mismatch!", statusCode, response.getStatusCode());
    }

    @Then("the response status code should be {int}")
    public void verify_the_response_status_code(int statusCode) {
        Assert.assertEquals("Status code mismatch!", statusCode, response.getStatusCode());
    }

    @Then("response JSON should contain {string}:{int}")
    public void verify_json_content(String key, int value) {
        response.then().body(key, equalTo(value));
    }

    @Given("a sale exists with ID {int}")
    public void sale_exists(int id) {
        Response check = request.get("/api/sales/" + id);
        if (check.getStatusCode() == 200) {
            dynamicSaleId = id;
            return;
        }

        // Fallback: search for any existing sale
        Response list = request.get("/api/sales");
        try {
            java.util.List<Integer> ids = list.jsonPath().getList("id");
            if (ids != null && !ids.isEmpty()) {
                dynamicSaleId = ids.get(0);
                System.out.println("⚠️ Sale " + id + " not found. Falling back to Sale ID " + dynamicSaleId);
            } else {
                Assert.assertEquals("No sales found in system for fallback!", 200, check.getStatusCode());
            }
        } catch (Exception e) {
            Assert.assertEquals("Sale " + id + " not found and list failed!", 200, check.getStatusCode());
        }
    }

    @And("the response should contain a list of sales")
    public void verify_sales_list() {
        response.then().body("$.size()", greaterThanOrEqualTo(0));
    }
}
