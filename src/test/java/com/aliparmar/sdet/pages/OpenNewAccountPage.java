package com.aliparmar.sdet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class OpenNewAccountPage extends BasePage {

    private static final By ACCOUNT_TYPE_DROPDOWN = By.id("type");
    private static final By FROM_ACCOUNT_DROPDOWN = By.id("fromAccountId");
    private static final By FROM_ACCOUNT_OPTIONS = By.cssSelector("#fromAccountId option");
    private static final By OPEN_ACCOUNT_BUTTON = By.cssSelector("input[value='Open New Account']");
    private static final By OPEN_ACCOUNT_RESULT = By.id("openAccountResult");
    private static final By OPEN_ACCOUNT_ERROR = By.id("openAccountError");
    private static final By NEW_ACCOUNT_ID = By.id("newAccountId");

    public OpenNewAccountPage(WebDriver driver) {
        super(driver);
    }

    public OpenNewAccountPage selectAccountType(String type) {
        Select dropdown = new Select(waitForElementVisible(ACCOUNT_TYPE_DROPDOWN));
        dropdown.selectByVisibleText(type);
        return this;
    }

    // Picks the first available "from" account without hardcoding an id,
    public OpenNewAccountPage selectFirstFromAccount() {
        waitForElementVisible(FROM_ACCOUNT_DROPDOWN);
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(FROM_ACCOUNT_OPTIONS, 0));
        new Select(driver.findElement(FROM_ACCOUNT_DROPDOWN)).selectByIndex(0);
        return this;
    }

    public void submit() {
        waitForElementClickable(OPEN_ACCOUNT_BUTTON).click();
    }

    // True only if the "Account Opened!" panel actually becomes visible.
    public boolean isResultDisplayed() {
        try {
            waitForElementVisible(OPEN_ACCOUNT_RESULT);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // True if the page is showing its "Error!" panel (useful in failure messages).
    public boolean isErrorDisplayed() {
        return driver.findElement(OPEN_ACCOUNT_ERROR).isDisplayed();
    }

    // Reads the new account number from the result panel.
    public String getNewAccountId() {
        return waitForElementVisible(NEW_ACCOUNT_ID).getText().trim();
    }
}