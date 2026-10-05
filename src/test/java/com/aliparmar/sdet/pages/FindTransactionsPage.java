package com.aliparmar.sdet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/*
 Page object for the Find Transactions page.
 All four search types (ID, Date, Date Range, Amount) live on one page —
 there are no tabs. Each has its own input and its own submit button.
 */
public class FindTransactionsPage extends BasePage {

    private static final By FIND_TRANSACTIONS_MENU_LINK = By.linkText("Find Transactions");
    private static final By TRANSACTION_ID_INPUT = By.id("transactionId");
    private static final By FIND_BY_ID_BUTTON = By.id("findById");
    private static final By AMOUNT_INPUT = By.id("amount");
    private static final By FIND_BY_AMOUNT_BUTTON = By.id("findByAmount");
    private static final By RESULTS_TABLE = By.id("transactionTable");

    public FindTransactionsPage(WebDriver driver) {
        super(driver);
    }

    // Navigates to the Find Transactions page from wherever the browser
    // currently is, using the Account Services menu link.
    public void open() {
        waitForElementClickable(FIND_TRANSACTIONS_MENU_LINK).click();
    }

    // Enters the transaction ID and clicks the "Find Transactions" button
    // for the Transaction ID search section specifically.
    public void searchByTransactionId(String transactionId) {
        waitForElementVisible(TRANSACTION_ID_INPUT).sendKeys(transactionId);
        waitForElementClickable(FIND_BY_ID_BUTTON).click();
    }

    // Enters an amount (e.g. "100.00", "$" stripped by the caller) and
    // clicks the "Find Transactions" button for the Amount search section.
    public void searchByAmount(String amount) {
        waitForElementVisible(AMOUNT_INPUT).sendKeys(amount);
        waitForElementClickable(FIND_BY_AMOUNT_BUTTON).click();
    }

    // Returns true if the results contain a link to the given transaction ID.
    public boolean resultsContainTransactionId(String transactionId) {
        By transactionLink = By.cssSelector("a[href*='id=" + transactionId + "']");
        return !driver.findElements(transactionLink).isEmpty();
    }

    // New method — exposes the raw results text for debugging failed assertions.
    public String getResultsText() {
        return waitForElementVisible(RESULTS_TABLE).getText();
    }
}