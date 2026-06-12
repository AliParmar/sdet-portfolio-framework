package com.aliparmar.sdet.pages;

import com.aliparmar.sdet.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page object for the ParaBank login page (which is also the home page).
 * Holds all locators and actions for logging in.
 */
public class LoginPage extends BasePage {

    // Locators kept in one place so updates only happen here when the UI changes.
    private static final By USERNAME_FIELD = By.name("username");
    private static final By PASSWORD_FIELD = By.name("password");
    private static final By LOGIN_BUTTON = By.cssSelector("input[value='Log In']");
    private static final By ERROR_MESSAGE = By.cssSelector(".error");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // Navigate to the login page using the base URL from config.
    public LoginPage open() {
        driver.get(ConfigReader.get("base.url"));
        return this;
    }

    public LoginPage enterUsername(String username) {
        waitForElementVisible(USERNAME_FIELD).sendKeys(username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        waitForElementVisible(PASSWORD_FIELD).sendKeys(password);
        return this;
    }

    public void clickLoginButton() {
        waitForElementClickable(LOGIN_BUTTON).click();
    }

    // Convenience method that combines all three login steps into one call.
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    public boolean isUsernameFieldDisplayed() {
        return !driver.findElements(USERNAME_FIELD).isEmpty();
    }

    public boolean isErrorMessageDisplayed() {
        return !driver.findElements(ERROR_MESSAGE).isEmpty();
    }
}