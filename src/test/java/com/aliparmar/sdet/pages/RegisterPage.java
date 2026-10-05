package com.aliparmar.sdet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.github.javafaker.*;


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
    private static final By RIGHT_PANEL = By.id("rightPanel");


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
        Faker faker = new Faker();
        waitForElementVisible(FIRST_NAME).sendKeys(faker.name().firstName());
        driver.findElement(LAST_NAME).sendKeys(faker.name().lastName());
        driver.findElement(STREET).sendKeys(faker.address().streetAddress());
        driver.findElement(CITY).sendKeys(faker.address().city());
        driver.findElement(STATE).sendKeys(faker.address().state());
        driver.findElement(ZIP_CODE).sendKeys(faker.address().zipCode());
        driver.findElement(PHONE).sendKeys(faker.phoneNumber().phoneNumber());
        driver.findElement(SSN).sendKeys(faker.number().digits(9));
        driver.findElement(USERNAME).sendKeys(username);
        driver.findElement(PASSWORD).sendKeys(password);
        driver.findElement(CONFIRM_PASSWORD).sendKeys(password);
        return this;
    }

    public String getPanelText() {
        return driver.findElement(RIGHT_PANEL).getText();
    }

    public void submit() {
        waitForElementClickable(REGISTER_BUTTON).click();
    }

    public boolean isRegistrationConfirmed() {
        try {
            wait.until(ExpectedConditions.textToBePresentInElementLocated(RIGHT_PANEL, "Welcome"));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

}