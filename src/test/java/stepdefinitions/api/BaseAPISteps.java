package stepdefinitions.api;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BaseAPISteps {
    protected static Response response;
    protected static RequestSpecification request;
    protected static final String BASE_URL = "http://localhost:8080";
}
