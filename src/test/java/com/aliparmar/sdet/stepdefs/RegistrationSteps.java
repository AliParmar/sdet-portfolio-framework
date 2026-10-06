package com.aliparmar.sdet.stepdefs;

import com.aliparmar.sdet.pages.LoginPage;
import com.aliparmar.sdet.pages.NavigationMenuPage;
import com.aliparmar.sdet.pages.RegisterPage;
import com.aliparmar.sdet.utils.DriverContext;
import com.aliparmar.sdet.utils.ScenarioContext;
import com.github.javafaker.Faker;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.aliparmar.sdet.utils.ScenarioContext;

import java.time.Duration;

/*
Step definitions for registration.feature.
Generates a unique username per run (via a timestamp) so repeated CI/local runs against the shared ParaBank demo don't collide on an existing username.
*/
public class RegistrationSteps {

    private static final String ACCOUNTS_OVERVIEW_TITLE = "ParaBank | Accounts Overview";

    private WebDriver driver;
    private RegisterPage registerPage;
    Faker faker = new Faker();
    private final String newUsername = "qa" + System.currentTimeMillis();
    private final String newPassword = faker.random().hex(12);

    public String getNewUsername(){
        return newUsername;
    }

    public String getNewPassword(){
        return newPassword;
    }

    @Given("I navigate to the registration page")
    public void i_navigate_to_the_registration_page() {
        driver = DriverContext.getDriver();
        registerPage = new RegisterPage(driver).open();
    }

    @And("I register a new customer with a unique username")
    public void i_register_a_new_customer_with_a_unique_username() {
        registerPage.fillRegistrationForm(newUsername, newPassword);
        registerPage.submit();
    }

    private final ScenarioContext context;

    public RegistrationSteps(ScenarioContext context) {
        this.context = context;
    }

    @Then("the registration should be confirmed")
    public void the_registration_should_be_confirmed() {
        Assert.assertTrue(
                registerPage.isRegistrationConfirmed(),
                "Expected registration to be confirmed for user '" + newUsername
                        + "'. Right panel text was: " + registerPage.getPanelText()
        );
        context.setCredentials(newUsername, newPassword);

    }
    @When("I log out")
    public void i_log_out() {
        new NavigationMenuPage(driver).logOut();
    }

    @And("I log in with the newly registered credentials")
    public void i_log_in_with_the_newly_registered_credentials() {
        new LoginPage(driver).login(newUsername, newPassword);
    }

    @Then("I should land on the Accounts Overview page")
    public void i_should_land_on_the_accounts_overview_page() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.titleIs(ACCOUNTS_OVERVIEW_TITLE));
        } catch (TimeoutException e) {
            String bodyText = driver.findElement(By.tagName("body")).getText();
            String shortBody = bodyText.length() > 400 ? bodyText.substring(0, 400) + "..." : bodyText;
            Assert.fail("Expected to land on Accounts Overview after logging in as '" + newUsername
                    + "' but title was [" + driver.getTitle() + "], url was [" + driver.getCurrentUrl()
                    + "]. Page text: " + shortBody);
        }
    }
}