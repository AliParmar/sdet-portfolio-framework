package com.aliparmar.sdet.stepdefs;

import com.aliparmar.sdet.utils.DriverContext;
import com.aliparmar.sdet.utils.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;

/**
 * Single source of truth for WebDriver setup and teardown.
 * Runs once per scenario, before any other step-def class needs the driver.
 */
public class Hooks {

    @Before
    public void setUp() {
        WebDriver driver = DriverFactory.createDriver();
        DriverContext.setDriver(driver);
    }

    @After
    public void tearDown() {
        WebDriver driver = DriverContext.getDriver();
        if (driver != null) {
            driver.quit();
        }
    }
}