package pages.care;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigManager;
import utils.DriverManager;

import java.time.Duration;
import java.util.List;

public class CareSearchPage {

    private WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    private WebDriverWait getWait() {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(15));
    }

    public void openUrl() {
        getDriver().get(ConfigManager.getConfig("baseUrl"));

    }


    public void selectToyotaBrand() throws InterruptedException {
        scrollDownMultipleTimes(2,300);

        WebDriverWait wait = getWait();
        wait.until(ExpectedConditions.elementToBeClickable(CareSearchLocators.TOYOTA_BRAND_IMG)).click();


      }



    public void scrollDownMultipleTimes(int times, int pixelsPerScroll) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();

        for (int i = 0; i < times; i++) {
            try {
                js.executeScript("window.scrollBy(0, arguments[0]);", pixelsPerScroll);
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Interrupted during scrolling: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error during scrolling: " + e.getMessage());
            }
        }
    }



    public void openYearFilter() {
        getWait().until(ExpectedConditions.elementToBeClickable(CareSearchLocators.YEAR_FILTER_BUTTON)).click();
    }
    public void clearInputField(By locator) {
        WebElement inputField = getDriver().findElement(locator);
        inputField.click();
        inputField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        inputField.sendKeys(Keys.DELETE);
    }
    public void setYearRange(String minYear, String maxYear) {
        WebElement minInput = getWait().until(ExpectedConditions.visibilityOfElementLocated(CareSearchLocators.YEAR_MIN_INPUT));
        clearInputField(CareSearchLocators.YEAR_MIN_INPUT);

        minInput.sendKeys(minYear);

        WebElement maxInput = getWait().until(ExpectedConditions.visibilityOfElementLocated(CareSearchLocators.YEAR_MAX_INPUT));
        clearInputField(CareSearchLocators.YEAR_MAX_INPUT);
        maxInput.sendKeys(maxYear);
    }

    public void clickDoneButton() {
        getWait().until(ExpectedConditions.elementToBeClickable(CareSearchLocators.DONE_BUTTON)).click();
    }

    public boolean validateToyotaSpanPresent() {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(CareSearchLocators.VALIDATE_TOYOTA_SPAN)).isDisplayed();
    }

    public boolean validateYearRangeSpanPresent() {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(CareSearchLocators.VALIDATE_YEAR_RANGE_SPAN)).isDisplayed();
    }

    public boolean validateHeadingPresent() {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(CareSearchLocators.VALIDATE_HEADING_H1)).isDisplayed();
    }

    public boolean isSearchResultsContainerPresent() {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(CareSearchLocators.SEARCH_RESULTS_CONTAINER)).isDisplayed();
    }

    public int getSearchResultsCount() {
        WebElement container = getWait().until(ExpectedConditions.visibilityOfElementLocated(CareSearchLocators.SEARCH_RESULTS_CONTAINER));
        List<WebElement> items = container.findElements(CareSearchLocators.SEARCH_RESULT_ITEMS);
        return items.size();
    }
}
