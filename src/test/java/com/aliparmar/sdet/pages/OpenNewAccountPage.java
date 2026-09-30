package com.aliparmar.sdet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

/**
 * Page object for the Open New Account page.
 * NOTE: locators below reflect ParaBank's documented markup (select#type,
 * select#fromAccountId, button#openAccountButton, div#openAccountResult).
 */
public class OpenNewAccountPage extends BasePage {

    private static final By ACCOUNT_TYPE_DROPDOWN = By.id("type");
    private static final By FROM_ACCOUNT_DROPDOWN = By.id("fromAccountId");
    private static final By OPEN_ACCOUNT_BUTTON = By.cssSelector("input[value='Open New Account']");
    private static final By NEW_ACCOUNT_ID = By.id("newAccountId");
    private static final By OPEN_ACCOUNT_RESULT = By.id("openAccountResult");

    public OpenNewAccountPage(WebDriver driver) {
        super(driver);
    }

    public OpenNewAccountPage selectAccountType(String type) {
        Select dropdown = new Select(waitForElementVisible(ACCOUNT_TYPE_DROPDOWN));
        dropdown.selectByVisibleText(type);
        return this;
    }

    // Picks the first available "from" account without hardcoding an id,
    // consistent with the framework's "don't hardcode account ids" decision.
    public OpenNewAccountPage selectFirstFromAccount() {
        Select dropdown = new Select(waitForElementVisible(FROM_ACCOUNT_DROPDOWN));
        dropdown.selectByIndex(0);
        return this;
    }

    public void submit() {
        waitForElementClickable(OPEN_ACCOUNT_BUTTON).click();
    }

    public boolean isResultDisplayed() {
        return !driver.findElements(OPEN_ACCOUNT_RESULT).isEmpty();
    }

    public String getNewAccountId() {
        return waitForElementVisible(NEW_ACCOUNT_ID).getText();
    }
}