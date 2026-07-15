package com.aliparmar.sdet.ui;

import com.aliparmar.sdet.base.BaseTest;
import com.aliparmar.sdet.pages.AccountsOverviewPage;
import com.aliparmar.sdet.pages.LoginPage;
import com.aliparmar.sdet.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Smoke tests for ParaBank.
 * Verifies the app is reachable and core elements load before other tests run.
 */
public class ParaBankSmokeTest extends BaseTest {

    @Test(groups = {"smoke"}, description = "Verify ParaBank home page loads and title is correct")
    public void verifyHomePageLoads() {
        new LoginPage(driver).open();

        String actualTitle = driver.getTitle();
        Assert.assertNotNull(actualTitle, "Page title should not be null");
        Assert.assertTrue(
                actualTitle.contains("ParaBank"),
                "Expected page title to contain 'ParaBank' but was: " + actualTitle
        );
    }

    @Test(groups = {"smoke"}, description = "Verify login form is present on the home page")
    public void verifyLoginFormPresent() {
        LoginPage loginPage = new LoginPage(driver).open();

        Assert.assertTrue(
                loginPage.isUsernameFieldDisplayed(),
                "Username field should be present on the home page"
        );
    }

    @Test(groups = {"smoke", "ui"}, description = "Valid login redirects to the Accounts Overview page")
    public void verifySuccessfulLoginRedirectsToOverview() {
        String username = ConfigReader.get("test.username");
        String password = ConfigReader.get("test.password");

        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.login(username, password);

        AccountsOverviewPage overviewPage = new AccountsOverviewPage(driver);

        Assert.assertTrue(
                overviewPage.getPageTitle().contains("Accounts Overview"),
                "Expected page title to contain 'Accounts Overview' after successful login, but was: " + overviewPage.getPageTitle()
        );
        Assert.assertTrue(
                overviewPage.isAccountTableDisplayed(),
                "Expected the accounts table to be displayed after successful login"
        );
    }

    @Test(groups = {"smoke", "ui"}, description = "Invalid login displays an error message instead of redirecting")
    public void verifyInvalidLoginShowsErrorMessage() {
        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.login("invalidUser", "wrongPassword123");

        Assert.assertTrue(
                loginPage.isErrorMessageDisplayed(),
                "Expected an error message to be displayed for invalid login credentials"
        );
    }

}