package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.testng.annotations.Test;
import pages.care.CareSearchPage;
import base.TestBase;
import static org.testng.Assert.assertTrue;

public class CareSearchSteps extends TestBase {

    CareSearchPage careSearchPage = new CareSearchPage();

    @Given("I open the car search page")
    @Test(description = "Prerequisite: Open car search page", groups = {"care"})
    public void i_open_the_car_search_page() {
        careSearchPage.openUrl();
    }

    @When("I select Toyota as the brand")
    @Test(description = "Select Toyota brand", groups = {"care"})
    public void i_select_toyota_as_the_brand() throws InterruptedException {
        careSearchPage.selectToyotaBrand();
    }

    @And("I set the year range from {string} to {string}")
    @Test(description = "Set year range filter", groups = {"care"})
    public void i_set_the_year_range_from_to(String minYear, String maxYear) {
        careSearchPage.openYearFilter();
        careSearchPage.setYearRange(minYear, maxYear);
        careSearchPage.clickDoneButton();
    }

    @Then("the search results should be filtered correctly")
    @Test(description = "Validate search results", groups = {"care"})
    public void the_search_results_should_be_filtered_correctly() {
        assertTrue(careSearchPage.validateToyotaSpanPresent(), "Toyota span is not visible");
        assertTrue(careSearchPage.validateYearRangeSpanPresent(), "Year range span is not visible");
        assertTrue(careSearchPage.validateHeadingPresent(), "Heading is not visible");
        assertTrue(careSearchPage.isSearchResultsContainerPresent(), "Search results container is not visible");

        int resultsCount = careSearchPage.getSearchResultsCount();
        assertTrue(resultsCount > 0, "Expected search results > 0 but found " + resultsCount);

        markCareSearchCompleted();
    }


    @Test(description = "Care Search Prerequisite - Complete car search flow",
          groups = {"care", "prerequisite"})
    public void completeCareSearchPrerequisite() {
        System.out.println("Care Search Prerequisite completed successfully");
    }
}
