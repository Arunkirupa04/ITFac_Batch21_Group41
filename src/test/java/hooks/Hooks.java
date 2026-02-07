package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import utils.DriverFactory;

public class Hooks {

    @Before("@ui")
    public void setupUI() {
        DriverFactory.initDriver();
    }

    @After("@ui")
    public void tearDownUI() {
        DriverFactory.quitDriver();
    }

}
