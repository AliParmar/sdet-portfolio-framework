package com.aliparmar.sdet.api;

import com.aliparmar.sdet.api.client.ParaBankApiClient;
import com.aliparmar.sdet.api.client.ParaBankApiClient.Account;
import com.aliparmar.sdet.api.client.ParaBankApiClient.BillPayResult;
import com.aliparmar.sdet.api.client.ParaBankApiClient.Transaction;
import com.aliparmar.sdet.base.BaseApiTest;
import com.aliparmar.sdet.utils.ConfigReader;
import org.testng.annotations.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

/*
Chained money-flow API tests (regression only). Every assertion is relative to a balance read at the start of the test, and the amounts only ever add money, so repeated runs never drain the account.
*/
public class ParaBankMoneyFlowApiTest extends BaseApiTest {

    private static final BigDecimal DEPOSIT = new BigDecimal("123.45");
    private static final BigDecimal WITHDRAWAL = new BigDecimal("23.45");
    private static final BigDecimal FUNDING = new BigDecimal("75.00");
    private static final BigDecimal BILL_AMOUNT = new BigDecimal("25.00");
    private static final String PAYEE = "Houston Power Co";

    private final ParaBankApiClient api = new ParaBankApiClient();

    private Account loginAndGetChecking() {
        int customerId = api.login(ConfigReader.get("test.username"), ConfigReader.get("test.password"));
        return api.findAccount(customerId, ParaBankApiClient.CHECKING);
    }

    @Test(groups = {"api", "regression"},
            description = "Deposit then withdraw: balance and transaction history stay consistent")
    public void depositThenWithdrawUpdatesBalanceAndHistory() {
        Account checking = loginAndGetChecking();
        int accountId = checking.id();
        BigDecimal start = checking.balance();
        int transactionsBefore = api.getTransactions(accountId).size();

        api.deposit(accountId, DEPOSIT);
        BigDecimal afterDeposit = start.add(DEPOSIT);
        assertThat("Balance after deposit", api.getAccount(accountId).balance(), comparesEqualTo(afterDeposit));

        api.withdraw(accountId, WITHDRAWAL);
        assertThat("Balance after withdrawal", api.getAccount(accountId).balance(),
                comparesEqualTo(afterDeposit.subtract(WITHDRAWAL)));

        List<Transaction> history = api.getTransactions(accountId);
        assertThat("Exactly two new transactions", history, hasSize(transactionsBefore + 2));
        assertThat("Credit amounts", amountsOfType(history, "Credit"), hasItem(comparesEqualTo(DEPOSIT)));
        assertThat("Debit amounts", amountsOfType(history, "Debit"), hasItem(comparesEqualTo(WITHDRAWAL)));
    }

    @Test(groups = {"api", "regression"},
            description = "Bill pay debits the source account and is searchable by amount")
    public void billPayDebitsAccountAndAppearsInHistory() {
        Account checking = loginAndGetChecking();
        int accountId = checking.id();
        BigDecimal start = checking.balance();

        api.deposit(accountId, FUNDING);                       // guarantees the payment is covered
        long debitsBefore = countDebits(api.getTransactionsByAmount(accountId, BILL_AMOUNT));

        BillPayResult result = api.billPay(accountId, BILL_AMOUNT, PAYEE);
        assertThat("Payee echoed back", result.payeeName(), equalTo(PAYEE));
        assertThat("Amount echoed back", result.amount(), comparesEqualTo(BILL_AMOUNT));

        int paidFrom = result.accountId();                     // chained from the bill-pay response
        assertThat("Balance after bill pay", api.getAccount(paidFrom).balance(),
                comparesEqualTo(start.add(FUNDING).subtract(BILL_AMOUNT)));

        long debitsAfter = countDebits(api.getTransactionsByAmount(paidFrom, BILL_AMOUNT));
        assertThat("One new Debit of the bill amount", debitsAfter, equalTo(debitsBefore + 1));
    }

    private static List<BigDecimal> amountsOfType(List<Transaction> transactions, String type) {
        return transactions.stream().filter(t -> type.equals(t.type())).map(Transaction::amount).toList();
    }

    private static long countDebits(List<Transaction> transactions) {
        return transactions.stream().filter(t -> "Debit".equals(t.type())).count();
    }
}