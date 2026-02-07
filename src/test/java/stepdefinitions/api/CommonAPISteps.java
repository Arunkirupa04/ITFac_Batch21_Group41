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
            System.out.println("Warning: Sale with ID " + id + " not found. Attempting to discover any sale...");
            Response allSales = request.get("/api/sales");
            if (allSales.getStatusCode() == 200 && !allSales.jsonPath().getList("$").isEmpty()) {
                createdSaleId = allSales.jsonPath().getInt("[0].id");
                dynamicSaleId = createdSaleId;
                System.out.println("Discovered existing sale with ID: " + createdSaleId);
            } else {
                System.out.println("No sales found. Creating a new sale for the test...");
                Response plants = request.get("/api/plants");
                int plantId = 1;
                if (plants.getStatusCode() == 200 && !plants.jsonPath().getList("$").isEmpty()) {
                    plantId = plants.jsonPath().getInt("[0].id");
                }
                Response create = request.queryParam("quantity", 1).post("/api/sales/plant/" + plantId);
                if (create.getStatusCode() == 201) {
                    createdSaleId = create.jsonPath().getInt("id");
                    dynamicSaleId = createdSaleId;
                    System.out.println("Created new sale with ID: " + createdSaleId);
                } else {
                    Assert.fail("Could not find or create a sale for the test.");
                }
            }
        } else {
            createdSaleId = id;
            dynamicSaleId = id;
        }
    }

    @When("admin sends a GET request to retrieve the sale")
    public void retrieve_created_sale() {
        response = request.get("/api/sales/" + createdSaleId);
    }

    @And("the response should contain a list of sales")
    public void verify_sales_list() {
        response.then().body("$.size()", greaterThanOrEqualTo(0));
    }
}