package pages.care;

import org.openqa.selenium.By;

public class CareSearchLocators {

    public static final By TOYOTA_BRAND_IMG = By.xpath("//a[contains(@href, 'toyota')]");


    // Year inputs
    public static final By YEAR_FILTER_BUTTON = By.xpath("//strong[text()='Year']");

    public static final By YEAR_MIN_INPUT = By.xpath("//input[@name='min']");
    public static final By YEAR_MAX_INPUT = By.xpath("//input[@name='max']");

    // Done button after year selection
    public static final By DONE_BUTTON = By.cssSelector("button.SubMenuContainer-module__DoneBtn");

    // Validation elements
    public static final By VALIDATE_TOYOTA_SPAN = By.xpath("//span[text()='Toyota']");
    public static final By VALIDATE_YEAR_RANGE_SPAN = By.xpath("//span[contains(text(),'2022 - 2025')]");
    public static final By VALIDATE_HEADING_H1 = By.xpath("//h1[contains(text(),'Toyota Cars for Sale')]");

    // Search results container
    public static final By SEARCH_RESULTS_CONTAINER = By.xpath("//*[@id='root']/div[2]/main/section/div/div/section[2]/div");

    // Each car card inside the container
    public static final By SEARCH_RESULT_ITEMS = By.xpath(".//div[@data-enabled='false' and contains(@class,'w-full')]");

}
