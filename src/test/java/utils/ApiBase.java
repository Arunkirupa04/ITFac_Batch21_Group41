package utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiBase {
    protected static final String BASE_URI = "http://localhost:8080/api"; // Adjust host/port as needed or load from
                                                                          // properties

    // Simulated tokens - In a real scenario, these would be fetched via a login
    // request
    private static final String ADMIN_TOKEN = "admin-token-placeholder";
    private static final String USER_TOKEN = "user-token-placeholder";

    public static RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .build();
    }

    public static RequestSpecification getAdminRequestSpec() {
        return getRequestSpec().header("Authorization", "Bearer " + ADMIN_TOKEN);
    }

    public static RequestSpecification getUserRequestSpec() {
        return getRequestSpec().header("Authorization", "Bearer " + USER_TOKEN);
    }

    // Shared state for steps (Simple approach for this task)
    public static io.restassured.response.Response response;
    public static RequestSpecification request;
}
