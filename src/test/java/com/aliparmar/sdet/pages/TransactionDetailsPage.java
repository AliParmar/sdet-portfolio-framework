package com.aliparmar.sdet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page object for the Transaction Details page.
 * This page has no element ids — values are read from the table cell
 * that follows each bolded label ("Transaction ID:", "Date:").
 */
public class TransactionDetailsPage extends BasePage {

    private static final By TRANSACTION_ID_VALUE =
            By.xpath("//td[b[text()='Transaction ID:']]/following-sibling::td[1]");
    private static final By TRANSACTION_DATE_VALUE =
            By.xpath("//td[b[text()='Date:']]/following-sibling::td[1]");

    public TransactionDetailsPage(WebDriver driver) {
        super(driver);
    }

    public String getTransactionId() {
        return waitForElementVisible(TRANSACTION_ID_VALUE).getText();
    }

    public String getTransactionDate() {
        return waitForElementVisible(TRANSACTION_DATE_VALUE).getText();
    }
}