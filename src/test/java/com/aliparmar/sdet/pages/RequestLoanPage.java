package com.aliparmar.sdet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


 // Page object for the Request Loan page.

public class RequestLoanPage extends BasePage {

    private static final By AMOUNT_FIELD = By.id("amount");
    private static final By DOWN_PAYMENT_FIELD = By.id("downPayment");
    private static final By FROM_ACCOUNT_DROPDOWN = By.id("fromAccountId");
    private static final By APPLY_BUTTON = By.cssSelector("input[value='Apply Now']");
    private static final By LOAN_STATUS = By.id("loanStatus");

    public RequestLoanPage(WebDriver driver) {
        super(driver);
    }

    public RequestLoanPage submitLoanRequest(String amount, String downPayment) {
        waitForElementVisible(AMOUNT_FIELD).sendKeys(amount);
        driver.findElement(DOWN_PAYMENT_FIELD).sendKeys(downPayment);
        // Leave fromAccountId at its default selection (first account)
        waitForElementClickable(APPLY_BUTTON).click();
        return this;
    }

    // Returns "Approved" or "Denied" - the test only asserts the flow completed
    // with one of the two valid outcomes, not a specific decision.
    public String getLoanStatus() {
        return waitForElementVisible(LOAN_STATUS).getText();
    }
}