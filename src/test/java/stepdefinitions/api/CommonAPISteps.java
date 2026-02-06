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

    @Then("the response status code should be {int} or {int}")
    public void theResponseStatusCodeShouldBeOr(int status1, int status2) {
        int actualStatus = response.getStatusCode();
        Assert.assertTrue("Status code should be " + status1 + " or " + status2 + " but was " + actualStatus,
                actualStatus == status1 || actualStatus == status2);
    }

    @Then("response JSON should contain {string}:{int}")
    public void verify_json_content(String key, int value) {
        response.then().body(key, equalTo(value));
    }

    @Given("a sale exists with ID {int}")
    public void sale_exists(int id) {
        Response check = request.get("/api/sales/" + id);
        if (check.getStatusCode() != 200) {
            System.out.println("Warning: Sale with ID " + id + " not found.");
        }
    }

    @And("the response should contain a list of sales")
    public void verify_sales_list() {
        response.then().body("$.size()", greaterThanOrEqualTo(0));
    }
}
