package com.aliparmar.sdet.tests;

import com.aliparmar.sdet.base.BaseTest;
import com.aliparmar.sdet.utils.ConfigReader;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Smoke tests for ParaBank.
 * Verifies the app is reachable and core elements load before other tests run.
 */
public class ParaBankSmokeTest extends BaseTest {

    @Test(groups = {"smoke"}, description = "Verify ParaBank home page loads and title is correct")
    public void verifyHomePageLoads() {
        driver.get(ConfigReader.get("base.url"));

        String actualTitle = driver.getTitle();
        Assert.assertTrue(
                actualTitle.contains("ParaBank"),
                "Expected page title to contain 'ParaBank' but was: " + actualTitle
        );
    }

    @Test(groups = {"smoke"}, description = "Verify login form is present on the home page")
    public void verifyLoginFormPresent() {
        driver.get(ConfigReader.get("base.url"));

        boolean usernameFieldExists = !driver.findElements(By.name("username")).isEmpty();

        Assert.assertTrue(usernameFieldExists, "Username field should be present on the home page");
    }
}