package com.aliparmar.sdet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

/*
 Page object for the Transfer Funds page.
 NOTE: locators reflect ParaBank's documented field ids. Confirm against the live DOM.
 Accounts are selected by dropdown INDEX rather than hardcoded account numbers,
 consistent with the framework's existing "don't hardcode account ids" decision.
 */
public class TransferFundsPage extends BasePage {

    private static final By AMOUNT_FIELD = By.id("amount");
    private static final By FROM_ACCOUNT_DROPDOWN = By.id("fromAccountId");
    private static final By TO_ACCOUNT_DROPDOWN = By.id("toAccountId");
    private static final By TRANSFER_BUTTON = By.cssSelector("input[value='Transfer']");
    private static final By RESULT_PANEL = By.id("showResult");

    public TransferFundsPage(WebDriver driver) {
        super(driver);
    }

    public TransferFundsPage enterAmount(String amount) {
        waitForElementVisible(AMOUNT_FIELD).sendKeys(amount);
        return this;
    }

    public TransferFundsPage selectFromAccountByIndex(int index) {
        new Select(driver.findElement(FROM_ACCOUNT_DROPDOWN)).selectByIndex(index);
        return this;
    }

    public TransferFundsPage selectToAccountByIndex(int index) {
        new Select(driver.findElement(TO_ACCOUNT_DROPDOWN)).selectByIndex(index);
        return this;
    }

    public void submit() {
        waitForElementClickable(TRANSFER_BUTTON).click();
    }

    public boolean isTransferConfirmed() {
        return !driver.findElements(RESULT_PANEL).isEmpty();
    }
}
