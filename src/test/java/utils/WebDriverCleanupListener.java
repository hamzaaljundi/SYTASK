package utils;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;


public class WebDriverCleanupListener implements ITestListener {
    
    @Override
    public void onTestStart(ITestResult result) {
        System.out.println(">> WebDriverCleanupListener: Test starting: " + result.getName() + " <<");
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println(">> WebDriverCleanupListener: Test passed: " + result.getName() + " <<");
        cleanupWebDriver();
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println(">> WebDriverCleanupListener: Test failed: " + result.getName() + " <<");
        cleanupWebDriver();
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println(">> WebDriverCleanupListener: Test skipped: " + result.getName() + " <<");
        cleanupWebDriver();
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println(">> WebDriverCleanupListener: Test failed but within success percentage: " + result.getName() + " <<");
        cleanupWebDriver();
    }
    
    @Override
    public void onStart(ITestContext context) {
        System.out.println(">> WebDriverCleanupListener: Test context starting: " + context.getName() + " <<");
    }
    
    @Override
    public void onFinish(ITestContext context) {
        System.out.println(">> WebDriverCleanupListener: Test context finished: " + context.getName() + " <<");
        cleanupWebDriver();
    }
    
    private void cleanupWebDriver() {
        try {
            DriverManager.quitDriver();
            System.out.println(">> WebDriverCleanupListener: WebDriver cleaned up for thread " + Thread.currentThread().getId() + " <<");
        } catch (Exception e) {
            System.out.println(">> WebDriverCleanupListener: Error cleaning up WebDriver: " + e.getMessage() + " <<");
        }
    }
} 