package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import utils.ConfigManager;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"steps", "base"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports.html",
        "utils.ExtentReportListener"
    },
    monochrome = true
)
public class TestNGCucumberRunner extends AbstractTestNGCucumberTests {

    private static boolean prerequisitesCompleted = false;
    private static final Object lock = new Object();

    // Phase 1: run  care search scenarios first
    @Test(description = "Phase 1: Care Search", groups = "prerequisites", priority = 1)
    public void runCareSearch() {
        System.out.println("=== PHASE 1: Running Care Search ===");
        
        String prereqThreadCount = ConfigManager.getConfig("prerequisite.thread.count");
        int threadCount = prereqThreadCount != null ? Integer.parseInt(prereqThreadCount) : 1;
        
        System.out.println("Running care search with " + threadCount + " thread(s)");
        
        synchronized (lock) {
            prerequisitesCompleted = true;
        }
        
        System.out.println("=== PHASE 1 COMPLETED: Care Search finished ===");
    }
    
    //Phase 2: run all scenarios in parallel (depends on prerequisites)

    @Test(description = "Phase 2: All Scenarios", dependsOnGroups = "prerequisites", priority = 2)
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
    


    

    @BeforeSuite(description = "Initialize test suite")
    public void initializeSuite() {
        synchronized (lock) {
            prerequisitesCompleted = false;
        }
        System.out.println(">> TestNGConfigListener: Test suite started. <<");
    }
    

    @AfterSuite(description = "All scenarios completed")
    public void afterAllScenarios() {
        System.out.println("=== PHASE 2 COMPLETED: All scenarios finished ===");
        System.out.println(">> TestNGConfigListener: Test suite completed. <<");
    }
} 