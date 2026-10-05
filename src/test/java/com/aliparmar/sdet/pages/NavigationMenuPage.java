package com.aliparmar.sdet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/*
 Page object for the left-hand navigation menu present on every
 authenticated ParaBank page (Accounts Overview, Transfer Funds, etc).
 Centralizing these links here avoids duplicating menu locators across
 every page object that needs to navigate away.
 */
public class NavigationMenuPage extends BasePage {

    private static final By OPEN_NEW_ACCOUNT_LINK = By.linkText("Open New Account");
    private static final By TRANSFER_FUNDS_LINK = By.linkText("Transfer Funds");
    private static final By BILL_PAY_LINK = By.linkText("Bill Pay");
    private static final By UPDATE_CONTACT_INFO_LINK = By.linkText("Update Contact Info");
    private static final By REQUEST_LOAN_LINK = By.linkText("Request Loan");
    private static final By LOG_OUT_LINK = By.cssSelector("[href='logout.htm']");

    public NavigationMenuPage(WebDriver driver) {
        super(driver);
    }

    public OpenNewAccountPage goToOpenNewAccount() {
        waitForElementClickable(OPEN_NEW_ACCOUNT_LINK).click();
        return new OpenNewAccountPage(driver);
    }

    public void goToTransferFunds() {
        waitForElementClickable(TRANSFER_FUNDS_LINK).click();
    }

    public BillPayPage goToBillPay() {
        waitForElementClickable(BILL_PAY_LINK).click();
        return new BillPayPage(driver);
    }

    public UpdateContactInfoPage goToUpdateContactInfo() {
        waitForElementClickable(UPDATE_CONTACT_INFO_LINK).click();
        return new UpdateContactInfoPage(driver);
    }

    public RequestLoanPage goToRequestLoan() {
        waitForElementClickable(REQUEST_LOAN_LINK).click();
        return new RequestLoanPage(driver);
    }

    public LoginPage logOut() {
        waitForElementClickable(LOG_OUT_LINK).click();
        return new LoginPage(driver);
    }
}
