package com.aliparmar.sdet.stepdefs;

import com.aliparmar.sdet.pages.AccountActivityPage;
import com.aliparmar.sdet.pages.AccountsOverviewPage;
import com.aliparmar.sdet.pages.FindTransactionsPage;
import com.aliparmar.sdet.pages.LoginPage;
import com.aliparmar.sdet.pages.TransactionDetailsPage;
import com.aliparmar.sdet.utils.DriverContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

/**
 * Step definitions for find_transactions.feature.
 * Carries state (account number, transaction ID, date) between steps
 * within a single scenario.
 */
public class FindTransactionsSteps {

    private WebDriver driver;
    private String transactionId;

    @Given("I log in as {string} with password {string}")
    public void i_log_in_as_with_password(String username, String password) {
        driver = DriverContext.getDriver();
        new LoginPage(driver).login(username, password);
    }

    @When("I open the activity page for my first account")
    public void i_open_the_activity_page_for_my_first_account() {
        AccountsOverviewPage overview = new AccountsOverviewPage(driver);
        overview.openActivityForFirstAccount();
    }

    @And("I open the last transaction on that account")
    public void i_open_the_last_transaction_on_that_account() {
        AccountActivityPage activity = new AccountActivityPage(driver);
        activity.openLastTransaction();
    }

    @Then("I capture the transaction ID and date from the transaction details page")
    public void i_capture_the_transaction_id_and_date_from_the_transaction_details_page() {
        TransactionDetailsPage details = new TransactionDetailsPage(driver);
        transactionId = details.getTransactionId();
        String transactionDate = details.getTransactionDate();

        System.out.println("Captured transaction ID: " + transactionId + ", date: " + transactionDate);
        Assert.assertNotNull(transactionId, "Captured transaction ID should not be null");
    }

    @When("I search Find Transactions using the captured transaction ID")
    public void i_search_find_transactions_using_the_captured_transaction_id() {
        FindTransactionsPage findTransactions = new FindTransactionsPage(driver);
        findTransactions.open();
        findTransactions.searchByTransactionId(transactionId);
    }

    @Then("the results should display the captured transaction ID")
    public void the_results_should_display_the_captured_transaction_id() {
        FindTransactionsPage findTransactions = new FindTransactionsPage(driver);
        boolean idFound = findTransactions.resultsContainTransactionId(transactionId);
        if (!idFound) {
            System.out.println("Actual results table text: [" + findTransactions.getResultsText() + "]");
        }
        Assert.assertTrue(idFound,
                "Expected transaction " + transactionId + " in Find Transactions results");
    }

}