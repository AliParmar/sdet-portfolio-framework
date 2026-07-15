package com.aliparmar.sdet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page object for the post-login Accounts Overview page.
 * Minimal by design — only holds what's needed to confirm a successful login flow.
 */
public class AccountsOverviewPage extends BasePage {

    private static final By ACCOUNT_TABLE = By.id("accountTable");
    private static final By FIRST_ACCOUNT_LINK = By.xpath("//table[@id='accountTable']/tbody/tr[1]/td[1]/a");

    public AccountsOverviewPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public boolean isAccountTableDisplayed() {
        return !driver.findElements(ACCOUNT_TABLE).isEmpty();
    }

    // Returns the account number shown in the first row of the account table.
    public String getFirstAccountNumber() {
        return driver.findElement(FIRST_ACCOUNT_LINK).getText();
    }

    // Clicks the first account number, which navigates to that account's Activity page.
    public void openActivityForFirstAccount() {
        driver.findElement(FIRST_ACCOUNT_LINK).click();
    }
}