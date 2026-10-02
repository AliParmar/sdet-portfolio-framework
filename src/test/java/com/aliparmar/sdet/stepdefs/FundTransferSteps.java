package com.aliparmar.sdet.stepdefs;

import com.aliparmar.sdet.pages.AccountsOverviewPage;
import com.aliparmar.sdet.pages.NavigationMenuPage;
import com.aliparmar.sdet.pages.TransferFundsPage;
import com.aliparmar.sdet.utils.DriverContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

/**
 * Step definitions for fund_transfer.feature.
 * "First"/"second" account refer to dropdown/table position (index 0/1),
 * matching the seeded demo data's account ordering - never a hardcoded
 * account number.
 */
public class FundTransferSteps {

    private static final double DELTA = 0.001;

    private WebDriver driver;
    private String firstAccountNumber;
    private String secondAccountNumber;
    private double firstAccountBalanceBefore;
    private double secondAccountBalanceBefore;

    @Given("I note the balances of my first and second accounts")
    public void i_note_the_balances_of_my_first_and_second_accounts() {
        driver = DriverContext.getDriver();
        AccountsOverviewPage overview = new AccountsOverviewPage(driver);

        firstAccountNumber = overview.getAccountNumberAtIndex(0);
        secondAccountNumber = overview.getAccountNumberAtIndex(1);
        firstAccountBalanceBefore = overview.getBalanceForAccount(firstAccountNumber);
        secondAccountBalanceBefore = overview.getBalanceForAccount(secondAccountNumber);
    }

    @When("I transfer {double} from my first account to my second account")
    public void i_transfer_amount_from_my_first_account_to_my_second_account(double amount) {
        new NavigationMenuPage(driver).goToTransferFunds();

        TransferFundsPage transferFundsPage = new TransferFundsPage(driver);
        transferFundsPage.enterAmount(String.valueOf(amount));
        transferFundsPage.selectFromAccountByIndex(0);
        transferFundsPage.selectToAccountByIndex(1);
        transferFundsPage.submit();

        Assert.assertTrue(transferFundsPage.isTransferConfirmed(), "Expected a transfer confirmation");
    }

    @Then("my first account balance should decrease by {double}")
    public void my_first_account_balance_should_decrease_by(double amount) {
        AccountsOverviewPage overview = new AccountsOverviewPage(driver);
        double actualBalance = overview.getBalanceForAccount(firstAccountNumber);
        Assert.assertEquals(
                actualBalance, firstAccountBalanceBefore - amount, DELTA,
                "Expected first account balance to decrease by " + amount
        );
    }

    @And("my second account balance should increase by {double}")
    public void my_second_account_balance_should_increase_by(double amount) {
        AccountsOverviewPage overview = new AccountsOverviewPage(driver);
        double actualBalance = overview.getBalanceForAccount(secondAccountNumber);
        Assert.assertEquals(
                actualBalance, secondAccountBalanceBefore + amount, DELTA,
                "Expected second account balance to increase by " + amount
        );
    }
}
