package runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "stepdefinitions.ui",
        plugin = {"pretty", "html:target/cucumber-report.html"},
        monochrome = true,
      tags = "@AdminSellValidation"  // <-- run only scenarios with this tag
)
public class UITestRunner {
}
