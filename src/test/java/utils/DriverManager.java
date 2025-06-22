package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class DriverManager {
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final List<WebDriver> allDrivers = new CopyOnWriteArrayList<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (WebDriver driver : allDrivers) {
                if (driver != null) {
                    driver.quit();
                }
            }
        }));
    }

    private DriverManager() {
    }

    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            long threadId = Thread.currentThread().getId();
            System.out.println("****** [DriverManager] Thread " + threadId + ": Creating new WebDriver instance. ******");

            String browser = ConfigManager.getConfig("browser").toLowerCase();
            
            switch (browser) {
                case "chrome":
                    driver = createChromeDriver();
                    break;
                case "firefox":
                    driver = createFirefoxDriver();
                    break;
                default:
                    throw new RuntimeException("Unsupported browser: " + browser);
            }
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driverThreadLocal.set(driver);
            allDrivers.add(driver);
        }
        return driver;
    }

    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver() {
        WebDriverManager.getInstance(DriverManagerType.FIREFOX).setup();
        FirefoxOptions options = new FirefoxOptions();
        return new FirefoxDriver(options);
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
            allDrivers.remove(driver);
        }
    }
}