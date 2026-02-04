package utils;

import io.restassured.RestAssured;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiBase {
    protected static final String BASE_URI = "http://localhost:8080/api"; // Adjust host/port as needed or load from
                                                                          // properties

    // Simulated tokens - In a real scenario, these would be fetched via a login
    // request
    private static String ADMIN_TOKEN;
    private static String USER_TOKEN;

    public static RequestSpecification getRequestSpec() {
        return RestAssured.given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON);
    }

    public static RequestSpecification getAdminRequestSpec() {
        if (ADMIN_TOKEN == null) {
            ADMIN_TOKEN = authenticate("admin", "admin123");
        }
        return getRequestSpec().header("Authorization", "Bearer " + ADMIN_TOKEN);
    }

    public static RequestSpecification getUserRequestSpec() {
        if (USER_TOKEN == null) {
            USER_TOKEN = authenticate("testuser", "test123");
        }
        return getRequestSpec().header("Authorization", "Bearer " + USER_TOKEN);
    }

    private static String authenticate(String username, String password) {
        // Try /auth/login first
        java.util.Map<String, String> credentials = new java.util.HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);

        try {
            return RestAssured.given()
                    .baseUri(BASE_URI)
                    .contentType(ContentType.JSON)
                    .body(credentials)
                    .post("/auth/login")
                    .then()
                    .statusCode(200)
                    .extract()
                    .path("token");
        } catch (AssertionError e) {
            // Fallback or just re-throw with message
            System.err.println("Authentication failed for user: " + username);
            throw e;
        }
    }

    // Shared state for steps (Simple approach for this task)
    public static io.restassured.response.Response response;
    public static RequestSpecification request;
}
