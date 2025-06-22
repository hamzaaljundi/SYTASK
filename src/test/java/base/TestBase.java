package base;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;


public class TestBase {
    protected WebDriver driver;

    private static boolean careSearchCompleted = false;


    protected static void markCareSearchCompleted() {
        careSearchCompleted = true;
        System.out.println("✓ Care Search completed");
    }
    



}