package com.aliparmar.sdet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;
/**
 * Page object for the post-login Accounts Overview page.
 * Extended beyond login-confirmation to support fund-transfer and
 * new-account verification scenarios: reading balances and checking
 * which account numbers are listed.
 */
public class AccountsOverviewPage extends BasePage {

    private static final By ACCOUNT_TABLE = By.id("accountTable");
    private static final By FIRST_ACCOUNT_LINK = By.xpath("//table[@id='accountTable']/tbody/tr[1]/td[1]/a");
    private static final By ACCOUNT_ROWS = By.xpath("//table[@id='accountTable']/tbody/tr");

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

    // Returns the account number in the row at the given zero-based index.
    // Used instead of hardcoding account numbers when a test just needs
    // "my first account" / "my second account".
    public String getAccountNumberAtIndex(int index) {
        List<WebElement> rows = waitForRows();
        return rows.get(index).findElement(By.tagName("a")).getText();
    }

    // Clicks the account number at the given index, opening its Activity page.
    public void openActivityForAccountAtIndex(int index) {
        List<WebElement> rows = waitForRows();
        rows.get(index).findElement(By.tagName("a")).click();
    }

    // Opens the Activity page for a specific account number (used after
    // opening a brand-new account, whose row position isn't guaranteed).
    public void openActivityForAccountNumber(String accountNumber) {
        By accountLink = By.xpath("//table[@id='accountTable']//a[text()='" + accountNumber + "']");
        driver.findElement(accountLink).click();
    }

    // Returns the balance (as a double, "$" and "," stripped) for the given account number.
    public double getBalanceForAccount(String accountNumber) {
        List<WebElement> rows = waitForRows();
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.get(0).getText().trim().equals(accountNumber)) {
                return parseCurrency(cells.get(1).getText());
            }
        }
        throw new IllegalArgumentException("Account " + accountNumber + " not found on Accounts Overview");
    }

    // Returns true if the given account number is listed on this page at all.
    public boolean isAccountListed(String accountNumber) {
        By accountLink = By.xpath("//table[@id='accountTable']//a[text()='" + accountNumber + "']");
        return !driver.findElements(accountLink).isEmpty();
    }

    private List<WebElement> waitForRows() {
        waitForElementVisible(ACCOUNT_TABLE);
        return driver.findElements(ACCOUNT_ROWS);
    }

    private double parseCurrency(String text) {
        return Double.parseDouble(text.replace("$", "").replace(",", "").trim());
    }
}