package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import utils.DriverFactory;

public class Hooks {

    @Before("@AdminSales")
    public void setup() {
        DriverFactory.initDriver();
    }

    @After("@AdminSales")
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    @Before("@AdminSellValidation or @UserSales or @UserAccess")
    public void setupGeneric() {
        DriverFactory.initDriver();
    }

    @After("@AdminSellValidation or @UserSales or @UserAccess")
    public void tearDownGeneric() {
        DriverFactory.quitDriver();
    }
}
