package com.aliparmar.sdet.stepdefs;

import com.aliparmar.sdet.pages.LoginPage;
import com.aliparmar.sdet.utils.DriverContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

/**
 * Step definitions for the ParaBank home page feature.
 * Each method matches a Gherkin step phrase in parabank_home.feature.
 */
public class HomePageSteps {

    private WebDriver driver;
    private LoginPage loginPage;

    @Given("I open the ParaBank home page")
    public void i_open_the_parabank_home_page() {
        driver = DriverContext.getDriver();
        loginPage = new LoginPage(driver);
        loginPage.open();
    }

    @Then("the page title should contain {string}")
    public void the_page_title_should_contain(String expectedText) {
        String actualTitle = driver.getTitle();
        Assert.assertNotNull(actualTitle, "Page title should not be null");
        Assert.assertTrue(
                actualTitle.contains(expectedText),
                "Expected page title to contain '" + expectedText + "' but was: " + actualTitle
        );
    }

    @Then("the username field should be displayed")
    public void the_username_field_should_be_displayed() {
        Assert.assertTrue(
                loginPage.isUsernameFieldDisplayed(),
                "Username field should be present on the home page"
        );
    }
}