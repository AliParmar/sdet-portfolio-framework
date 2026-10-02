package com.aliparmar.sdet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page object for the Registration page.
 * NOTE: locators reflect ParaBank's documented field names (customer.firstName,
 * customer.username, etc). Confirm against the live DOM before relying on them.
 */
public class RegisterPage extends BasePage {

    private static final By REGISTER_MENU_LINK = By.linkText("Register");
    private static final By FIRST_NAME = By.id("customer.firstName");
    private static final By LAST_NAME = By.id("customer.lastName");
    private static final By STREET = By.id("customer.address.street");
    private static final By CITY = By.id("customer.address.city");
    private static final By STATE = By.id("customer.address.state");
    private static final By ZIP_CODE = By.id("customer.address.zipCode");
    private static final By PHONE = By.id("customer.phoneNumber");
    private static final By SSN = By.id("customer.ssn");
    private static final By USERNAME = By.id("customer.username");
    private static final By PASSWORD = By.id("customer.password");
    private static final By CONFIRM_PASSWORD = By.id("repeatedPassword");
    private static final By REGISTER_BUTTON = By.cssSelector("input[value='Register']");
    private static final By RESULT_PANEL = By.id("rightPanel");
    private static final By ERROR_MESSAGE = By.cssSelector(".error");

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    // Navigates to the Registration page via the top-nav "Register" link,
    // assumed to be visible on the current (logged-out) page.
    public RegisterPage open() {
        waitForElementClickable(REGISTER_MENU_LINK).click();
        return this;
    }

    // Fills every required field with fixed sample data except username,
    // which is supplied by the caller so each test run gets a unique customer.
    public RegisterPage fillRegistrationForm(String username, String password) {
        waitForElementVisible(FIRST_NAME).sendKeys("Ali");
        driver.findElement(LAST_NAME).sendKeys("Portfolio");
        driver.findElement(STREET).sendKeys("456 Portfolio Ave");
        driver.findElement(CITY).sendKeys("Houston");
        driver.findElement(STATE).sendKeys("TX");
        driver.findElement(ZIP_CODE).sendKeys("77001");
        driver.findElement(PHONE).sendKeys("7135551234");
        driver.findElement(SSN).sendKeys("123456789");
        driver.findElement(USERNAME).sendKeys(username);
        driver.findElement(PASSWORD).sendKeys(password);
        driver.findElement(CONFIRM_PASSWORD).sendKeys(password);
        return this;
    }

    public void submit() {
        waitForElementClickable(REGISTER_BUTTON).click();
    }

    public boolean isRegistrationConfirmed() {
        if (driver.findElements(RESULT_PANEL).isEmpty()) {
            return false;
        }
        String resultText = driver.findElement(RESULT_PANEL).getText();
        return resultText.toLowerCase().contains("success") || resultText.toLowerCase().contains("welcome");
    }

    public boolean isValidationErrorDisplayed() {
        return !driver.findElements(ERROR_MESSAGE).isEmpty();
    }
}