package com.aliparmar.sdet.tests;

import com.aliparmar.sdet.base.BaseTest;
import com.aliparmar.sdet.pages.LoginPage;
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

}