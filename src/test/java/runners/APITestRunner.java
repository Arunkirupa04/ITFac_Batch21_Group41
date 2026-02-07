package runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/api", glue = "stepdefinitions.api", plugin = { "pretty",
        "html:target/api-report.html" }, monochrome = true
//tags = "@admin"
)

public class APITestRunner {
}
