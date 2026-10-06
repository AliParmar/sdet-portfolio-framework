package com.aliparmar.sdet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/*
 Page object for the Transfer Funds page.
 Accounts are selected by dropdown INDEX rather than hardcoded account numbers.
 */
public class TransferFundsPage extends BasePage {

    private static final By AMOUNT_FIELD = By.id("amount");
    private static final By FROM_ACCOUNT_DROPDOWN = By.id("fromAccountId");
    private static final By TO_ACCOUNT_DROPDOWN = By.id("toAccountId");
    private static final By FROM_ACCOUNT_OPTIONS = By.cssSelector("#fromAccountId option");
    private static final By TO_ACCOUNT_OPTIONS = By.cssSelector("#toAccountId option");
    private static final By TRANSFER_BUTTON = By.cssSelector("input[value='Transfer']");
    private static final By RESULT_PANEL = By.id("showResult");
    private static final By ERROR_PANEL = By.id("showError");

    public TransferFundsPage(WebDriver driver) {
        super(driver);
    }

    public TransferFundsPage enterAmount(String amount) {
        waitForElementVisible(AMOUNT_FIELD).sendKeys(amount);
        return this;
    }

    // Waits until the dropdown has more than 'index' options
    public TransferFundsPage selectFromAccountByIndex(int index) {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(FROM_ACCOUNT_OPTIONS, index));
        new Select(driver.findElement(FROM_ACCOUNT_DROPDOWN)).selectByIndex(index);
        return this;
    }

    public TransferFundsPage selectToAccountByIndex(int index) {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(TO_ACCOUNT_OPTIONS, index));
        new Select(driver.findElement(TO_ACCOUNT_DROPDOWN)).selectByIndex(index);
        return this;
    }

    public void submit() {
        waitForElementClickable(TRANSFER_BUTTON).click();
    }

    // True only once the "Transfer Complete!" panel is actually visible.
    public boolean isTransferConfirmed() {
        try {
            waitForElementVisible(RESULT_PANEL);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // True if the page is showing its "Error!" panel.
    public boolean isErrorDisplayed() {
        return driver.findElement(ERROR_PANEL).isDisplayed();
    }
}