package com.aliparmar.sdet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page object for the Find Transactions page.
 * All four search types (ID, Date, Date Range, Amount) live on one page —
 * there are no tabs. Each has its own input and its own submit button.
 */
public class FindTransactionsPage extends BasePage {

    private static final By FIND_TRANSACTIONS_MENU_LINK = By.linkText("Find Transactions");
    private static final By TRANSACTION_ID_INPUT = By.id("transactionId");
    private static final By FIND_BY_ID_BUTTON = By.id("findById");
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

    // Returns true if the results contain a link to the given transaction ID.
    // Note: the Date column is not checked here — ParaBank displays a
    // different date for the same transaction ID between the Transaction
    // Details page and the Find Transactions results table (observed
    // discrepancy: 07-14-2026 vs 07-13-2026 for the same transaction).
    // Transaction ID alone is the reliable, unique identifier.
    public boolean resultsContainTransactionId(String transactionId) {
        By transactionLink = By.cssSelector("a[href*='id=" + transactionId + "']");
        return !driver.findElements(transactionLink).isEmpty();
    }

    // New method — exposes the raw results text for debugging failed assertions.
    public String getResultsText() {
        return waitForElementVisible(RESULTS_TABLE).getText();
    }
}