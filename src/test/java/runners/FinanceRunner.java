package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
    features = "src/test/resources/features/FinanceEligibility.feature",
    glue = {"steps", "base"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports-finance.html",
        "utils.ExtentReportListener"
    },
    monochrome = true
)
public class FinanceRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
} 