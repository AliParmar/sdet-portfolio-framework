package com.aliparmar.sdet.stepdefs;

import com.aliparmar.sdet.api.client.ParaBankApiClient;
import com.aliparmar.sdet.api.client.ParaBankApiClient.Account;
import com.aliparmar.sdet.utils.ScenarioContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

/** API steps that continue from the UI registration scenario using the shared credentials. */
public class MoneyFlowApiSteps {

    private final ScenarioContext context;
    private final ParaBankApiClient api = new ParaBankApiClient();

    private int customerId;
    private Account checking;
    private int savingsId;
    private BigDecimal checkingBaseline;
    private BigDecimal savingsBaseline;

    public MoneyFlowApiSteps(ScenarioContext context) {
        this.context = context;
    }

    @When("I log in to the API with the newly registered credentials")
    public void i_log_in_to_the_api() {
        customerId = api.login(context.getUsername(), context.getPassword());
    }

    @Then("the customer should have a checking account with a positive balance")
    public void the_customer_should_have_a_checking_account() {
        checking = api.findAccount(customerId, ParaBankApiClient.CHECKING);
        assertThat("Opening checking balance", checking.balance(), greaterThan(BigDecimal.ZERO));
    }

    @When("I open a new savings account funded from the checking account")
    public void i_open_a_new_savings_account() {
        Account created = api.createAccount(customerId, ParaBankApiClient.SAVINGS_TYPE_CODE, checking.id());
        assertThat("Type of the account created", created.type(), equalTo(ParaBankApiClient.SAVINGS));
        savingsId = created.id();
        // Baselines are read AFTER funding, so we never assume ParaBank's opening deposit.
        checkingBaseline = api.getAccount(checking.id()).balance();
        savingsBaseline = api.getAccount(savingsId).balance();
    }

    @And("I transfer {bigdecimal} from checking to the new savings account")
    public void i_transfer(BigDecimal amount) {
        api.transfer(checking.id(), savingsId, amount);
    }

    @Then("the checking balance should be reduced by {bigdecimal}")
    public void checking_balance_reduced(BigDecimal amount) {
        assertThat("Checking balance", api.getAccount(checking.id()).balance(),
                comparesEqualTo(checkingBaseline.subtract(amount)));
    }

    @And("the savings balance should be increased by {bigdecimal}")
    public void savings_balance_increased(BigDecimal amount) {
        assertThat("Savings balance", api.getAccount(savingsId).balance(),
                comparesEqualTo(savingsBaseline.add(amount)));
    }

    @And("the combined balance of both accounts should be unchanged by the transfer")
    public void combined_balance_unchanged() {
        BigDecimal before = checkingBaseline.add(savingsBaseline);
        BigDecimal after = api.getAccount(checking.id()).balance().add(api.getAccount(savingsId).balance());
        assertThat("Combined balance (money neither created nor lost)", after, comparesEqualTo(before));
    }
}