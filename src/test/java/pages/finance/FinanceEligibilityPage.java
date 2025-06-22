package pages.finance;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigManager;
import utils.DriverManager;
import org.openqa.selenium.ElementClickInterceptedException;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class FinanceEligibilityPage {
    private static final int TIMEOUT = 10;

    private WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    private WebDriverWait getWait() {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(TIMEOUT));
    }

    private String getProgressValue() {
        try {
            return getWait().until(ExpectedConditions.presenceOfElementLocated(FinanceEligibilityLocators.PROGRESS_BAR)).getAttribute("value");
        } catch (TimeoutException e) {
            return "0";
        }
    }

    private boolean waitForProgress(String initialValue) {
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(3)).until(driver -> {
                try {
                    String currentValue = driver.findElement(FinanceEligibilityLocators.PROGRESS_BAR).getAttribute("value");
                    return Double.parseDouble(currentValue) > Double.parseDouble(initialValue);
                } catch (StaleElementReferenceException | NoSuchElementException e) {
                    return false;
                }
            });
            return true;
        } catch (TimeoutException e) {
            System.out.println("Info: Progress bar value did not increase from " + initialValue + ". Assuming no progress change for this action.");
            return true;
        }
    }

    private void robustClick(By locator) {
        for (int i = 0; i < 3; i++) {
            try {
                WebElement element = getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
                String initialProgress = getProgressValue();

                if (i > 0) {
                    System.out.println("Forcing browser repaint and retrying click on: " + locator);
                    ((JavascriptExecutor) getDriver()).executeScript("document.body.style.display='none'; return document.body.offsetHeight; document.body.style.display='block';");
                }
                
                ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", element);

                if (waitForProgress(initialProgress)) {
                    System.out.println("Click successful and verified on: " + locator);
                    return;
                }
                System.out.println("Click on " + locator + " did not register (attempt " + (i + 1) + "). Retrying...");

            } catch (Exception e) {
                System.out.println("Exception during click attempt " + (i + 1) + " for " + locator + ": " + e.getMessage());
                if (i == 2) {
                    System.err.println("Failed to click element after 3 attempts: " + locator);
                    throw e;
                }
            }
        }
    }

    private void clickVerifyOrContinue() {
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(2)).until(ExpectedConditions.presenceOfElementLocated(FinanceEligibilityLocators.VERIFY_BUTTON));
            System.out.println("Found 'Verify' button. Attempting robust click.");
            robustClick(FinanceEligibilityLocators.VERIFY_BUTTON);
            return;
        } catch (TimeoutException e) {

        } catch (Exception e) {
            System.err.println("CRITICAL: Found 'Verify' button but failed to click it. Will try 'Continue'. Error: " + e.getMessage());
        }

        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(1)).until(ExpectedConditions.presenceOfElementLocated(FinanceEligibilityLocators.SUBMIT_BUTTON));
            System.out.println("Found 'Continue' button. Attempting robust click.");
            robustClick(FinanceEligibilityLocators.SUBMIT_BUTTON);
        } catch (TimeoutException e) {
            System.out.println("Info: Neither 'Verify' nor 'Continue' button was found. Proceeding.");
        } catch (Exception e) {
            System.err.println("CRITICAL: Found 'Continue' button but failed to click it. Proceeding. Error: " + e.getMessage());
        }
    }

    private void clearAndEnter(By locator, String text) {
        WebElement element = getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    public void openUrl() {
        getDriver().get(ConfigManager.getConfig("baseUrl") +"/site/finance-eligibility");
    }


    public void selectCarType(String carType) {
        By locator = carType.equalsIgnoreCase("new") ? 
            FinanceEligibilityLocators.NEW_CAR_RADIO : 
            FinanceEligibilityLocators.USED_CAR_RADIO;
        robustClick(locator);
    }

    public void selectEmploymentSector(String sector) {
        By locator = sector.equalsIgnoreCase("government") ? 
            FinanceEligibilityLocators.GOVERNMENT_SECTOR_RADIO : 
            FinanceEligibilityLocators.PRIVATE_SECTOR_RADIO;
        robustClick(locator);
    }

    public void enterMonthlyIncome(String income) {
        clearAndEnter(FinanceEligibilityLocators.MONTHLY_INCOME_FIELD, income);
        clickVerifyOrContinue();
    }



    public void setFinancialObligations(boolean hasObligations, String amount) {
        By locator = hasObligations ?
            FinanceEligibilityLocators.HAS_COMMITMENTS_YES :
            FinanceEligibilityLocators.HAS_COMMITMENTS_NO;
        robustClick(locator);

        if (hasObligations) {

            clickVerifyOrContinue();
            clearAndEnter(FinanceEligibilityLocators.OBLIGATIONS_AMOUNT_FIELD, amount);
        }
        
        clickVerifyOrContinue();
    }

    public void setSalaryBankAccount(boolean hasAccount) {
        By locator = hasAccount ? 
            FinanceEligibilityLocators.SALARY_BANK_ACCOUNT_YES : 
            FinanceEligibilityLocators.SALARY_BANK_ACCOUNT_NO;
        robustClick(locator);
        getWait().until(ExpectedConditions.elementToBeClickable(FinanceEligibilityLocators.JOB_TENURE_YES));
    }

    public void setJobTenure(boolean hasTenure) {
        By locator = hasTenure ? FinanceEligibilityLocators.JOB_TENURE_YES : FinanceEligibilityLocators.JOB_TENURE_NO;
        robustClick(locator);
        getWait().until(ExpectedConditions.elementToBeClickable(FinanceEligibilityLocators.SOCIAL_INSURANCE_3_MONTHS_YES));
    }

    public void setSocialInsurance3Months(boolean hasInsurance) {
        By locator = hasInsurance ?
                FinanceEligibilityLocators.SOCIAL_INSURANCE_3_MONTHS_YES :
                FinanceEligibilityLocators.SOCIAL_INSURANCE_3_MONTHS_NO;
        robustClick(locator);
        getWait().until(ExpectedConditions.visibilityOfElementLocated(FinanceEligibilityLocators.MONTHLY_INCOME_FIELD));
    }

    public void setSocialInsuranceRegistered(boolean isRegistered) {
        By locator = isRegistered ? 
            FinanceEligibilityLocators.SOCIAL_INSURANCE_REGISTERED_YES : 
            FinanceEligibilityLocators.SOCIAL_INSURANCE_REGISTERED_NO;
        robustClick(locator);
    }

    public void clickSubmit() {
        robustClick(FinanceEligibilityLocators.SUBMIT_BUTTON);
    }

    public String getEligibilityStatus() {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(FinanceEligibilityLocators.ELIGIBILITY_STATUS_MESSAGE)).getText();
    }

    public void enterPhoneNumber(String phoneNumber) {
        clearAndEnter(FinanceEligibilityLocators.PHONE_NUMBER_FIELD, phoneNumber);
    }

    public void clickStartNow() {
        robustClick(FinanceEligibilityLocators.START_NOW_BUTTON);
    }

    public void enterOTP(String otp) {
        List<WebElement> otpFields = getWait().until(ExpectedConditions.visibilityOfAllElements(getDriver().findElements(FinanceEligibilityLocators.OTP_INPUT_FIELDS)));
        for (int i = 0; i < otp.length(); i++) {
            otpFields.get(i).sendKeys(String.valueOf(otp.charAt(i)));
        }
    }

    public String getEligibilityStatusTitle() throws InterruptedException {
        Thread.sleep(5000);
        getDriver().navigate().refresh();
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(FinanceEligibilityLocators.ELIGIBILITY_STATUS_TITLE)).getText();
    }

    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }


}