package com.aliparmar.sdet.utils;

import org.openqa.selenium.WebDriver;

/**
 * Holds the WebDriver instance for the current scenario so multiple
 * step-def classes can share one driver instead of each creating its own.
 */
public class DriverContext {

    private static WebDriver driver;

    public static void setDriver(WebDriver newDriver) {
        driver = newDriver;
    }

    public static WebDriver getDriver() {
        return driver;
    }
}