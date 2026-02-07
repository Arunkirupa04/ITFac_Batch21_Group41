package runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/ui", glue = "stepdefinitions.ui", plugin = { "pretty",
        "html:target/cucumber-report-specific.html",
        "json:target/cucumber-report-specific.json" }, monochrome = true, tags = "@plants and @admin")

public class SpecificTestRunner {
}
