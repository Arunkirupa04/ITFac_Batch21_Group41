package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import utils.DbCleanHelper;
import utils.DriverFactory;

public class Hooks {

    /** Runs once before the first scenario; DB clean is optional (see db.properties, db.enabled=false by default). */
    @Before(order = -1000)
    public void runDbCleanOnceBeforeSuite() {
        DbCleanHelper.runOnce();
    }

    @Before("@ui")
    public void setupUI() {
        DriverFactory.initDriver();
    }

    @After("@ui")
    public void tearDownUI() {
        DriverFactory.quitDriver();
    }

    @Before("@AdminSell or @AdminSellValidation or @UserSales or @UserAccess")
    public void setupGeneric() {
        DriverFactory.initDriver();
    }

    @After("@AdminSell or @AdminSellValidation or @UserSales or @UserAccess")
    public void tearDownGeneric() {
        DriverFactory.quitDriver();
    }
}
