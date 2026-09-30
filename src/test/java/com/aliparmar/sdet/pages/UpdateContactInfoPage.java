package com.aliparmar.sdet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page object for the Update Contact Info page.
 * NOTE: locators reflect ParaBank's documented field names.
 */
public class UpdateContactInfoPage extends BasePage {

    private static final By STREET_FIELD = By.id("customer.address.street");
    private static final By CITY_FIELD = By.id("customer.address.city");
    private static final By UPDATE_BUTTON = By.cssSelector("input[value='Update Profile']");
    private static final By SUCCESS_MESSAGE = By.cssSelector("#updateProfileResult>h1");

    public UpdateContactInfoPage(WebDriver driver) {
        super(driver);
    }

    public UpdateContactInfoPage updateStreetAndCity(String street, String city) {
        waitForElementVisible(STREET_FIELD).clear();
        driver.findElement(STREET_FIELD).sendKeys(street);
        driver.findElement(CITY_FIELD).clear();
        driver.findElement(CITY_FIELD).sendKeys(city);
        return this;
    }

    public void submit() {
        waitForElementClickable(UPDATE_BUTTON).click();
    }

    public String getConfirmationText() {
        return waitForElementVisible(SUCCESS_MESSAGE).getText();
    }
}