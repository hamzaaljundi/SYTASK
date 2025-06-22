package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestRunStarted;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestStepFinished;
import io.cucumber.plugin.event.TestStepStarted;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ExtentReportListener implements ConcurrentEventListener {
    
    private static ExtentReports extent;
    private static Map<String, ExtentTest> featureTestMap = new HashMap<>();
    private static Map<String, ExtentTest> scenarioTestMap = new HashMap<>();
    private static Map<String, ExtentTest> stepTestMap = new HashMap<>();
    
    public ExtentReportListener() {
        if (extent == null) {
            initializeExtentReports();
        }
    }
    
    private void initializeExtentReports() {
        extent = new ExtentReports();
        
        File reportDir = new File("target/extent-report");
        if (!reportDir.exists()) {
            reportDir.mkdirs();
        }
        
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("target/extent-report/ExtentSpark.html");
        
        sparkReporter.config().setTheme(com.aventstack.extentreports.reporter.configuration.Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("Test Automation Report");
        sparkReporter.config().setReportName("Automation Test Results");
        sparkReporter.config().setTimeStampFormat("dd/MM/yyyy hh:mm:ss");
        
        try {
            File customCssFile = new File("src/test/resources/extent-custom.css");
            if (customCssFile.exists()) {
                String customCss = new String(java.nio.file.Files.readAllBytes(customCssFile.toPath()));
                sparkReporter.config().setCss(customCss);
                System.out.println("Custom CSS loaded for Extent Report");
            }
        } catch (Exception e) {
            System.out.println("Failed to load custom CSS: " + e.getMessage());
        }
        
        extent.attachReporter(sparkReporter);
        
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("Browser", ConfigManager.getConfig("browser"));
        extent.setSystemInfo("Base URL", ConfigManager.getConfig("baseUrl"));
        extent.setSystemInfo("Test Framework", "TestNG + Cucumber");
    }
    
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, this::handleTestRunStarted);
        publisher.registerHandlerFor(TestRunFinished.class, this::handleTestRunFinished);
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
        publisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinished);
        publisher.registerHandlerFor(TestStepStarted.class, this::handleTestStepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
    }
    
    private void handleTestRunStarted(TestRunStarted event) {
        System.out.println(">> ExtentReportListener: Test run started <<");
    }
    
    private void handleTestRunFinished(TestRunFinished event) {
        if (extent != null) {
            extent.flush();
            System.out.println(">> ExtentReportListener: Test run finished, report generated <<");
        }
    }
    
    private void handleTestCaseStarted(TestCaseStarted event) {
        String scenarioName = event.getTestCase().getName();
        String featureName = event.getTestCase().getUri().getPath();
        
        ExtentTest featureTest = featureTestMap.computeIfAbsent(featureName, 
            k -> extent.createTest("Feature: " + new File(k).getName().replace(".feature", "")));
        
        ExtentTest scenarioTest = featureTest.createNode("Scenario: " + scenarioName);
        scenarioTestMap.put(scenarioName, scenarioTest);
        
        System.out.println(">> ExtentReportListener: Started scenario: " + scenarioName + " <<");
    }
    
    private void handleTestCaseFinished(TestCaseFinished event) {
        String scenarioName = event.getTestCase().getName();
        ExtentTest scenarioTest = scenarioTestMap.get(scenarioName);
        
        if (scenarioTest != null) {
            if (event.getResult().getStatus().name().equals("PASSED")) {
                scenarioTest.log(Status.PASS, "Scenario passed successfully");
            } else if (event.getResult().getStatus().name().equals("FAILED")) {
                String errorMessage = "Scenario failed: " + event.getResult().getError().getMessage();
                scenarioTest.log(Status.FAIL, errorMessage);
                
                addScreenshotToReport(scenarioTest, scenarioName);
            } else {
                scenarioTest.log(Status.SKIP, "Scenario skipped");
            }
        }
        
        System.out.println(">> ExtentReportListener: Finished scenario: " + scenarioName + " <<");
    }
    
    private void handleTestStepStarted(TestStepStarted event) {
        String stepName = getStepText(event);
        String scenarioName = event.getTestCase().getName();
        
        ExtentTest scenarioTest = scenarioTestMap.get(scenarioName);
        if (scenarioTest != null) {
            ExtentTest stepTest = scenarioTest.createNode("Step: " + stepName);
            stepTestMap.put(stepName + "_" + scenarioName, stepTest);
        }
    }
    
    private void handleTestStepFinished(TestStepFinished event) {
        String stepName = getStepText(event);
        String scenarioName = event.getTestCase().getName();
        String stepKey = stepName + "_" + scenarioName;
        
        ExtentTest stepTest = stepTestMap.get(stepKey);
        if (stepTest != null) {
            if (event.getResult().getStatus().name().equals("PASSED")) {
                stepTest.log(Status.PASS, "Step executed successfully");
            } else if (event.getResult().getStatus().name().equals("FAILED")) {
                String errorMessage = "Step failed: " + event.getResult().getError().getMessage();
                stepTest.log(Status.FAIL, errorMessage);
                
                addScreenshotToReport(stepTest, scenarioName);
            } else {
                stepTest.log(Status.SKIP, "Step skipped");
            }
        }
    }
    
    private String getStepText(io.cucumber.plugin.event.TestStepStarted event) {
        try {
            if (event.getTestStep() instanceof io.cucumber.plugin.event.PickleStepTestStep) {
                io.cucumber.plugin.event.PickleStepTestStep pickleStep = (io.cucumber.plugin.event.PickleStepTestStep) event.getTestStep();
                return pickleStep.getStep().getText();
            }
        } catch (Exception e) {
            System.out.println("Could not extract step text: " + e.getMessage());
        }
        

        String stepName = event.getTestStep().getCodeLocation();
        return getStepDescription(stepName);
    }
    
    private String getStepText(io.cucumber.plugin.event.TestStepFinished event) {
        try {
            if (event.getTestStep() instanceof io.cucumber.plugin.event.PickleStepTestStep) {
                io.cucumber.plugin.event.PickleStepTestStep pickleStep = (io.cucumber.plugin.event.PickleStepTestStep) event.getTestStep();
                return pickleStep.getStep().getText();
            }
        } catch (Exception e) {
            System.out.println("Could not extract step text: " + e.getMessage());
        }
        
        String stepName = event.getTestStep().getCodeLocation();
        return getStepDescription(stepName);
    }
    
    private String getStepDescription(String stepName) {

        if (stepName != null && stepName.contains(".")) {
            String methodName = stepName.substring(stepName.lastIndexOf(".") + 1);
            return methodName.replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase();
        }
        return stepName != null ? stepName : "Unknown Step";
    }
    
    private void addScreenshotToReport(ExtentTest test, String scenarioName) {
        try {
            File screenshotDir = new File("target/extent-report/screenshots");
            if (screenshotDir.exists()) {
                File[] screenshotFiles = screenshotDir.listFiles((dir, name) -> 
                    name.toLowerCase().endsWith(".png") && 
                    name.contains(scenarioName.replaceAll("[^a-zA-Z0-9]", "_")));
                
                if (screenshotFiles != null && screenshotFiles.length > 0) {
                    File latestScreenshot = screenshotFiles[0];
                    for (File file : screenshotFiles) {
                        if (file.lastModified() > latestScreenshot.lastModified()) {
                            latestScreenshot = file;
                        }
                    }
                    
                    test.addScreenCaptureFromPath("screenshots/" + latestScreenshot.getName(), "Screenshot");
                    System.out.println("Screenshot added to report: " + latestScreenshot.getName());
                } else {
                    takeScreenshotFromWebDriver(test, scenarioName);
                }
            } else {
                takeScreenshotFromWebDriver(test, scenarioName);
            }
        } catch (Exception e) {
            System.out.println("Failed to add screenshot to report: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void takeScreenshotFromWebDriver(ExtentTest test, String scenarioName) {
        try {
            org.openqa.selenium.WebDriver driver = DriverManager.getDriver();
            if (driver != null) {
                File extentScreenshotDir = new File("target/extent-report/screenshots");
                if (!extentScreenshotDir.exists()) {
                    extentScreenshotDir.mkdirs();
                }
                
                String screenshotName = scenarioName.replaceAll("[^a-zA-Z0-9]", "_") + "_" + System.currentTimeMillis() + ".png";
                String screenshotPath = "target/extent-report/screenshots/" + screenshotName;
                org.openqa.selenium.TakesScreenshot ts = (org.openqa.selenium.TakesScreenshot) driver;
                java.io.File screenshot = ts.getScreenshotAs(org.openqa.selenium.OutputType.FILE);
                
                java.nio.file.Files.copy(screenshot.toPath(), new java.io.File(screenshotPath).toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                
                test.addScreenCaptureFromPath("screenshots/" + screenshotName, "Screenshot");
                System.out.println("Screenshot taken and added to report: " + screenshotPath);
            }
        } catch (Exception e) {
            System.out.println("Failed to take screenshot from WebDriver: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 