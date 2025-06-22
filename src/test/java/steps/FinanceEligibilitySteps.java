package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import pages.finance.FinanceEligibilityPage;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class FinanceEligibilitySteps {
    private final FinanceEligibilityPage financePage;
    private final Map<String, String> formData;
    private static final String RESULTS_FILE = "FinanceEligibilityResults.xlsx";

    public FinanceEligibilitySteps() {
        this.financePage = new FinanceEligibilityPage();
        this.formData = new LinkedHashMap<>();
    }

    @Given("I open the finance eligibility form page")
    public void openFormPage() {
        try {
            financePage.openUrl();
        } catch (Exception e) {
            throw e;
        }
    }

    @When("I select car type as {string} and employment sector as {string}")
    public void selectCarTypeAndEmploymentSector(String carType, String sector) {
        financePage.selectCarType(carType);
        financePage.selectEmploymentSector(sector);
        formData.put("Car Type", carType);
        formData.put("Employment Sector", sector);
    }

    @When("I enter monthly income as {string}")
    public void enterIncome(String income) {
        validateNumericInput("Monthly Income", income);
        financePage.enterMonthlyIncome(income);
        formData.put("Monthly Income", income);
    }

    @When("I set financial obligations to {string} with amount {string}")
    public void setObligations(String hasObligations, String amount) throws InterruptedException {
        validateYesNoInput("Has Obligations", hasObligations);

        boolean hasObligationsBool = "yes".equalsIgnoreCase(hasObligations);

        financePage.setFinancialObligations(hasObligationsBool, amount);

        formData.put("Has Obligations", hasObligations);
        if (hasObligationsBool) {
            validateNumericInput("Obligations Amount", amount);
            formData.put("Obligations Amount", amount);
        }
    }

    @When("I set salary bank account to {string}")
    public void setBankAccount(String hasAccount) {
        validateYesNoInput("Salary Bank Account", hasAccount);
        boolean hasAccountBool = "yes".equalsIgnoreCase(hasAccount);
        financePage.setSalaryBankAccount(hasAccountBool);
        formData.put("Salary Bank Account", hasAccount);
    }

    @When("I set job tenure to {string}")
    public void setJobTenure(String hasTenure) {
        validateYesNoInput("Job Tenure", hasTenure);
        boolean hasTenureBool = "yes".equalsIgnoreCase(hasTenure);
        financePage.setJobTenure(hasTenureBool);
        formData.put("Job Tenure (3+ months)", hasTenure);
    }

    @When("I set social insurance \\(3+ months) to {string}")
    public void setSocialInsurance(String hasInsurance) {
        validateYesNoInput("Social Insurance (3+ months)", hasInsurance);
        boolean hasInsuranceBool = "yes".equalsIgnoreCase(hasInsurance);
        financePage.setSocialInsurance3Months(hasInsuranceBool);
        formData.put("Social Insurance (3+ months)", hasInsurance);
    }

    @When("I set social insurance registration to {string}")
    public void setInsuranceRegistration(String isRegistered) {
        validateYesNoInput("Social Insurance Registered", isRegistered);
        boolean isRegisteredBool = "yes".equalsIgnoreCase(isRegistered);
        financePage.setSocialInsuranceRegistered(isRegisteredBool);
        formData.put("Social Insurance Registered", isRegistered);
    }

    @When("I submit the form")
    public void submitForm() {
        try {
            financePage.clickSubmit();
        } catch (Exception e) {
            throw e;
        }
    }

    @Then("the eligibility status should be {string}")
    public void verifyEligibilityStatus(String expectedStatus) {
        String actualStatus = financePage.getEligibilityStatus().toLowerCase().trim();
        assertEquals("Eligibility status mismatch. Expected: " + expectedStatus + "\\n" +
                        ", Actual: " + actualStatus,
                expectedStatus.toLowerCase(),
                actualStatus);
    }

    @When("I enter phone number as {string}")
    public void iEnterPhoneNumberAs(String phoneNumber) {
        financePage.enterPhoneNumber(phoneNumber);
    }

    @When("I click on Start Now button")
    public void iClickOnStartNowButton() {
        financePage.clickStartNow();
    }

    @When("I enter OTP as {string}")
    public void iEnterOTPAs(String otp) {
        financePage.enterOTP(otp);
    }

    @Then("the eligibility status title should be {string} and URL path should be {string}")
    public void theEligibilityStatusTitleShouldBeAndURLPathShouldBe(String expectedTitle, String expectedUrlPath) throws InterruptedException {
        String actualTitle = financePage.getEligibilityStatusTitle();
        String actualUrl = financePage.getCurrentUrl();
System.out.println("Actrual tiltle:: "+actualTitle);

        assertEquals(actualTitle,expectedTitle);
        assertTrue(actualUrl.contains(expectedUrlPath), "URL path mismatch. Expected to contain: " + expectedUrlPath + ", Actual: " + actualUrl);
    }

    // Helper methods
    private void validateYesNoInput(String fieldName, String value) {
        if (!value.equalsIgnoreCase("yes") && !value.equalsIgnoreCase("no")) {
            throw new IllegalArgumentException(fieldName + " must be 'yes' or 'no'");
        }
    }

    private void validateNumericInput(String fieldName, String value) {
        if (!value.matches("\\d+")) {
            throw new IllegalArgumentException(fieldName + " must be numeric");
        }
    }


}