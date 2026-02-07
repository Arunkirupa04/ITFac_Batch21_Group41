package stepdefinitions.api;

import utils.ApiBase;

public class BaseAPISteps extends ApiBase {
    protected static final String BASE_URL = "http://localhost:8080";
    protected static String adminToken;
    protected static String userToken;
    protected static int createdSaleId;
    protected static int foundPlantId;
    protected static Integer dynamicSaleId;
}
