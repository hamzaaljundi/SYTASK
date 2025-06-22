package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.JavascriptExecutor;
import utils.ConfigManager;
import utils.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Hooks {
    
    private static boolean careSearchCompleted = false;
    private WebDriver driver;
    
    // run before care search scenarios complete successfully

    @Before("@care")
    public void beforeCareSearch() {
        System.out.println("Starting Care Search scenario...");
    }

    // run after care search scenarios complete successfully
    @After("@care")
    public void afterCareSearch(Scenario scenario) {
        System.out.println("Care Search scenario completed: " + scenario.getName());
        if (scenario.isFailed()) {
            takeScreenshot(scenario, "care_search");
        }
        careSearchCompleted = true;
    }
    
    // run before finance scenarios

    @Before("@finance")
    public void beforeFinance(Scenario scenario) {
        System.out.println("Starting Finance scenario: " + scenario.getName());
    }

    // run after finance scenarios

    @After("@finance")
    public void afterFinance(Scenario scenario) {
        System.out.println("Finance scenario completed: " + scenario.getName());
        if (scenario.isFailed()) {
            takeScreenshot(scenario, "finance");
        }
    }

    @Before
    public void setUp() {
        driver = DriverManager.getDriver();
        if (driver != null) {
            driver.manage().window().maximize();
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            takeScreenshot(scenario, "general");
        }
        // WebDriver cleanup is handled by WebDriverCleanupListener
    }
    
    private void takeScreenshot(Scenario scenario, String prefix) {
        try {
            if (driver != null) {
                File screenshotDir = new File("target/extent-report/screenshots");
                if (!screenshotDir.exists()) {
                    screenshotDir.mkdirs();
                }
                
                String scenarioName = scenario.getName().replaceAll("[^a-zA-Z0-9]", "_");
                String timestamp = String.valueOf(System.currentTimeMillis());
                String screenshotPath = "target/extent-report/screenshots/" + prefix + "_" + scenarioName + "_" + timestamp + ".png";
                
                TakesScreenshot ts = (TakesScreenshot) driver;
                File screenshot = ts.getScreenshotAs(OutputType.FILE);
                
                Files.copy(screenshot.toPath(), new File(screenshotPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
                
                byte[] screenshotBytes = Files.readAllBytes(new File(screenshotPath).toPath());
                scenario.attach(screenshotBytes, "image/png", "Screenshot");
                
                System.out.println("Screenshot saved: " + screenshotPath);
            }
        } catch (IOException e) {
            System.out.println("Failed to take screenshot: " + e.getMessage());
        }
    }
}
