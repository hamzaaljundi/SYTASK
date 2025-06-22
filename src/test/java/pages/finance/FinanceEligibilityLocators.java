package pages.finance;

import org.openqa.selenium.By;

public class FinanceEligibilityLocators {


    // For "Used car" radio button
    public static final By USED_CAR_RADIO = By.xpath("//label[contains(.,'Used car')]//input[@type='radio']");

    // For "New car" radio button
    public static final By NEW_CAR_RADIO = By.xpath("//label[contains(.,'New car')]//input[@type='radio']");
    // Employment Sector Selection
    public static final By PRIVATE_SECTOR_RADIO = By.xpath("//label[contains(.,'Private sector')]//input[@type='radio']");
    public static final By GOVERNMENT_SECTOR_RADIO = By.xpath("//label[contains(.,'Government sector')]//input[@type='radio']");

    // Income and Obligations
    public static final By MONTHLY_INCOME_FIELD = By.xpath("//input[@inputmode='numeric' and @pattern='[0-9]*']");


    // Job Tenure
    public static final By JOB_TENURE_YES = By.cssSelector("#root > div:nth-child(3) > form > div > div.isLoaded.MainContainerLO > main > div > div > div.transition-all.duration-700.ease-in-out.transform.translate-y-0.opacity-100 > div > div > div > div > div.bg-white.rounded-lg.border.border-solid.border-gray-azureish.py-6.px-4.flex.flex-col.gap-8.lg\\:border-0.lg\\:p-0 > div.flex.flex-col.gap-3 > div > label:nth-child(1)");

    public static final By JOB_TENURE_NO = By.cssSelector("#root > div:nth-child(3) > form > div > div.isLoaded.MainContainerLO > main > div > div > div.transition-all.duration-700.ease-in-out.transform.translate-y-0.opacity-100 > div > div > div > div > div.bg-white.rounded-lg.border.border-solid.border-gray-azureish.py-6.px-4.flex.flex-col.gap-8.lg\\:border-0.lg\\:p-0 > div.flex.flex-col.gap-3 > div > label:nth-child(2)");

    // Salary Bank Account
    public static final By SALARY_BANK_ACCOUNT_YES = By.xpath("//label[contains(@class, 'flex gap-3') and .//input[@value='true'] and contains(., 'Yes')]");
    public static final By SALARY_BANK_ACCOUNT_NO = By.xpath("//label[contains(@class, 'flex gap-3') and .//input[@value='false'] and contains(., 'No')]");

    // Social Insurance Registered
    public static final By SOCIAL_INSURANCE_REGISTERED_YES =
            By.xpath("//label[contains(@class,'flex gap-3')][contains(.,'Yes')]");

    public static final By SOCIAL_INSURANCE_REGISTERED_NO =
            By.xpath("//label[contains(@class,'flex gap-3')][contains(.,'No')]");
    // Social Insurance 3 Months
    public static final By SOCIAL_INSURANCE_3_MONTHS_YES =
            By.xpath("//label[.//input[@value='true'] and contains(., 'Yes')]");

    public static final By SOCIAL_INSURANCE_3_MONTHS_NO =
            By.xpath("//label[.//input[@value='false'] and contains(., 'No')]");

    // Has COMMITMENTS
    public static final By HAS_COMMITMENTS_YES = By.xpath("//label[contains(.,'Yes, I have commitments')]//input");
    public static final By HAS_COMMITMENTS_NO = By.xpath("//label[contains(.,'No commitments')]//input");


    // Obligations Amount
    public static final By OBLIGATIONS_AMOUNT_FIELD = By.xpath("//input[contains(@class, 'InputCurrency-module__inputField') and @inputmode='numeric']");
    // Submit Button
    public static final By SUBMIT_BUTTON = By.xpath("//button[contains(text(), 'Continue')]");
    public static final By VERIFY_BUTTON = By.xpath("//button[contains(text(), 'Verify')]");

    // Eligibility Status Message
    public static final By ELIGIBILITY_STATUS_MESSAGE = By.id("eligibility-message");


    // Phone Number and OTP
    public static final By PHONE_NUMBER_FIELD = By.xpath("//input[@inputmode='numeric' and @maxlength='10' and contains(@class, 'InputPhoneNumber-module__inputField')]");
    public static final By START_NOW_BUTTON = By.xpath("//button[contains(.,'Start now')]");
    public static final By OTP_INPUT_FIELDS = By.cssSelector(".Otp-module__optInput");
    public static final By ELIGIBILITY_STATUS_TITLE = By.xpath("//h2[contains(@class, 'text-primary')]");

    // Progress Bar
    public static final By PROGRESS_BAR = By.id("progress");
}