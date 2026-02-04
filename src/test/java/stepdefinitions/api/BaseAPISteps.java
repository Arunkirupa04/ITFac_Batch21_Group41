package stepdefinitions.api;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BaseAPISteps {
    protected static Response response;
    protected static RequestSpecification request;
    protected static final String BASE_URL = "http://localhost:8080";
    protected static String adminToken;
    protected static String userToken;
    protected static int createdSaleId;
    protected static int foundPlantId;
}
