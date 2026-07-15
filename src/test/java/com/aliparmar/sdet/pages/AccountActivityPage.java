package com.aliparmar.sdet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page object for the Account Activity page.
 * Locators confirmed against live DOM via DevTools.
 */
public class AccountActivityPage extends BasePage {

    private static final By TRANSACTION_TABLE = By.id("transactionTable");
    private static final By TRANSACTION_ROWS = By.xpath("//table[@id='transactionTable']/tbody/tr");

    public AccountActivityPage(WebDriver driver) {
        super(driver);
    }

    // Clicks the link in the last rendered row, opening Transaction Details.
    public void openLastTransaction() {
        waitForElementVisible(TRANSACTION_TABLE);
        List<WebElement> rows = driver.findElements(TRANSACTION_ROWS);
        WebElement lastRow = rows.getLast();
        lastRow.findElement(By.tagName("a")).click();
    }
}