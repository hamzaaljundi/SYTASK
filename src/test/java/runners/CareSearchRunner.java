package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
    features = "src/test/resources/features/care_search.feature",
    glue = {"steps", "base"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports-care.html",
        "utils.ExtentReportListener"
    },
    monochrome = true
)
public class CareSearchRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
} 