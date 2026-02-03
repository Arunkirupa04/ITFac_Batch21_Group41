package stepdefinitions.api;

import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static org.hamcrest.Matchers.*;

public class BaseSteps {
    // Shared variables
    public static RequestSpecification request;
    public static Response response;

    @Then("response code should be {int}")
    public void response_code_should_be(Integer statusCode) {
        response.then().statusCode(statusCode);
    }

    @Then("response JSON should contain {string}:{int}")
    public void response_json_should_contain(String key, Integer value) {
        response.then().body(key, equalTo(value));
    }

    @Then("response message should contain {string}")
    public void response_message_should_contain(String expectedMsg) {
        response.then().body("message", containsString(expectedMsg));
    }

    @Then("response should contain validation error")
    public void response_should_contain_validation_error() {
        response.then()
                .body("error", equalTo("UNAUTHORIZED"))
                .body("message", containsString("Unauthorized"));
    }
}