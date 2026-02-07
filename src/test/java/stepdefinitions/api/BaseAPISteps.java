package stepdefinitions.api;

import utils.ApiBase;

public class BaseAPISteps extends ApiBase {
    protected static final String BASE_URL = "http://localhost:8080";
    protected static String adminToken;
    protected static String userToken;
    protected static int createdSaleId;
    protected static int foundPlantId;
    protected static Integer dynamicSaleId;
    /** Existing category ID fetched for user (GET /api/categories); used so tests don't rely on id=1. */
    protected static Integer existingCategoryId;
    /** Category ID created for TC_API_ADMIN_07 (retrieve by ID); avoid hardcoded id=1. */
    protected static Integer categoryIdForAdminRetrieval;
}
