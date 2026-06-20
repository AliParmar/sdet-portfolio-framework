package com.aliparmar.sdet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page object for the post-login Accounts Overview page.
 * Minimal by design — only holds what's needed to confirm a successful login flow.
 */
public class AccountsOverviewPage extends BasePage {

    private static final By ACCOUNT_TABLE = By.id("accountTable");

    public AccountsOverviewPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public boolean isAccountTableDisplayed() {
        return !driver.findElements(ACCOUNT_TABLE).isEmpty();
    }
}