package com.aliparmar.sdet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

/*
 Page object for the Bill Pay page.
 NOTE: locators reflect ParaBank's documented field names
 (payee.name, payee.address.street, etc).
 */
public class BillPayPage extends BasePage {

    private static final By PAYEE_NAME = By.name("payee.name");
    private static final By PAYEE_STREET = By.name("payee.address.street");
    private static final By PAYEE_CITY = By.name("payee.address.city");
    private static final By PAYEE_STATE = By.name("payee.address.state");
    private static final By PAYEE_ZIP = By.name("payee.address.zipCode");
    private static final By PAYEE_PHONE = By.name("payee.phoneNumber");
    private static final By PAYEE_ACCOUNT = By.name("payee.accountNumber");
    private static final By VERIFY_ACCOUNT = By.name("verifyAccount");
    private static final By AMOUNT = By.name("amount");
    private static final By FROM_ACCOUNT_DROPDOWN = By.id("fromAccountId");
    private static final By SEND_PAYMENT_BUTTON = By.cssSelector("input[value='Send Payment']");
    private static final By ERROR_MESSAGE = By.cssSelector(".error");
    private static final By PAYMENT_COMPLETE_HEADER = By.cssSelector("h1.title");

    public BillPayPage(WebDriver driver) {
        super(driver);
    }

    // Fills every field except payee name, used by the negative validation test.
    public BillPayPage fillFormWithoutPayeeName(String accountNumber, String amount) {
        waitForElementVisible(PAYEE_STREET).sendKeys("123 Main St");
        driver.findElement(PAYEE_CITY).sendKeys("Houston");
        driver.findElement(PAYEE_STATE).sendKeys("TX");
        driver.findElement(PAYEE_ZIP).sendKeys("77001");
        driver.findElement(PAYEE_PHONE).sendKeys("7135551234");
        driver.findElement(PAYEE_ACCOUNT).sendKeys(accountNumber);
        driver.findElement(VERIFY_ACCOUNT).sendKeys(accountNumber);
        driver.findElement(AMOUNT).sendKeys(amount);
        return this;
    }

    // Fills every field including payee name - the happy-path payment used
    // by the Cucumber Bill Pay journey. Pays from the account at fromAccountIndex
    // (0 = first account) rather than a hardcoded account number.
    public BillPayPage fillFormWithValidPayee(String accountNumber, String amount, int fromAccountIndex) {
        waitForElementVisible(PAYEE_NAME).sendKeys("Electric Company");
        driver.findElement(PAYEE_STREET).sendKeys("123 Main St");
        driver.findElement(PAYEE_CITY).sendKeys("Houston");
        driver.findElement(PAYEE_STATE).sendKeys("TX");
        driver.findElement(PAYEE_ZIP).sendKeys("77001");
        driver.findElement(PAYEE_PHONE).sendKeys("7135551234");
        driver.findElement(PAYEE_ACCOUNT).sendKeys(accountNumber);
        driver.findElement(VERIFY_ACCOUNT).sendKeys(accountNumber);
        driver.findElement(AMOUNT).sendKeys(amount);
        new Select(driver.findElement(FROM_ACCOUNT_DROPDOWN)).selectByIndex(fromAccountIndex);
        return this;
    }

    public void submit() {
        waitForElementClickable(SEND_PAYMENT_BUTTON).click();
    }

    public boolean isValidationErrorDisplayed() {
        return !driver.findElements(ERROR_MESSAGE).isEmpty();
    }

    public String getResultHeaderText() {
        return waitForElementVisible(PAYMENT_COMPLETE_HEADER).getText();
    }
}