package com.aliparmar.sdet.api.client;

import com.aliparmar.sdet.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Thin REST Assured wrapper over the ParaBank money endpoints. Money is always BigDecimal. */
public class ParaBankApiClient {

    public static final String CHECKING = "CHECKING";
    public static final String SAVINGS = "SAVINGS";
    /** newAccountType code for POST /createAccount (spec says integer only; Test 1 asserts the result). */
    public static final int SAVINGS_TYPE_CODE = 1;

    public record Account(int id, String type, BigDecimal balance) { }
    public record Transaction(int id, int accountId, String type, BigDecimal amount) { }
    public record BillPayResult(String payeeName, BigDecimal amount, int accountId) { }

    private static final JsonPathConfig EXACT_NUMBERS =
            JsonPathConfig.jsonPathConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL);

    private RequestSpecification spec() {
        return RestAssured.given()
                .baseUri(ConfigReader.get("api.base.url"))
                .accept(ContentType.JSON);
    }

    private JsonPath json(Response response) {
        return response.jsonPath().using(EXACT_NUMBERS);
    }

    private static BigDecimal money(Object value) {
        return new BigDecimal(String.valueOf(value));
    }

    // ---------- Customer / accounts ----------

    /** Logs in and returns the customer id. */
    public int login(String username, String password) {
        Response response = spec()
                .pathParam("username", username)
                .pathParam("password", password)
                .when().get("/login/{username}/{password}")
                .then().log().ifValidationFails().statusCode(200)
                .extract().response();
        return json(response).getInt("id");
    }

    public List<Account> getAccounts(int customerId) {
        Response response = spec()
                .pathParam("customerId", customerId)
                .when().get("/customers/{customerId}/accounts")
                .then().log().ifValidationFails().statusCode(200)
                .extract().response();
        JsonPath jp = json(response);
        List<Integer> ids = jp.getList("id");
        List<String> types = jp.getList("type");
        List<Object> balances = jp.getList("balance");
        List<Account> accounts = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            accounts.add(new Account(ids.get(i), types.get(i), money(balances.get(i))));
        }
        return accounts;
    }

    public Account findAccount(int customerId, String type) {
        return getAccounts(customerId).stream()
                .filter(a -> type.equals(a.type()))
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "No " + type + " account found for customer " + customerId));
    }

    public Account getAccount(int accountId) {
        Response response = spec()
                .pathParam("accountId", accountId)
                .when().get("/accounts/{accountId}")
                .then().log().ifValidationFails().statusCode(200)
                .extract().response();
        JsonPath jp = json(response);
        return new Account(jp.getInt("id"), jp.getString("type"), money(jp.get("balance")));
    }

    public Account createAccount(int customerId, int accountTypeCode, int fromAccountId) {
        Response response = spec()
                .queryParam("customerId", customerId)
                .queryParam("newAccountType", accountTypeCode)
                .queryParam("fromAccountId", fromAccountId)
                .when().post("/createAccount")
                .then().log().ifValidationFails().statusCode(200)
                .extract().response();
        JsonPath jp = json(response);
        return new Account(jp.getInt("id"), jp.getString("type"), money(jp.get("balance")));
    }

    // ---------- Money movement (responses are plain strings, so we only check the status) ----------

    public void transfer(int fromAccountId, int toAccountId, BigDecimal amount) {
        spec().queryParam("fromAccountId", fromAccountId)
                .queryParam("toAccountId", toAccountId)
                .queryParam("amount", amount.toPlainString())
                .when().post("/transfer")
                .then().log().ifValidationFails().statusCode(200);
    }

    public void deposit(int accountId, BigDecimal amount) {
        spec().queryParam("accountId", accountId)
                .queryParam("amount", amount.toPlainString())
                .when().post("/deposit")
                .then().log().ifValidationFails().statusCode(200);
    }

    public void withdraw(int accountId, BigDecimal amount) {
        spec().queryParam("accountId", accountId)
                .queryParam("amount", amount.toPlainString())
                .when().post("/withdraw")
                .then().log().ifValidationFails().statusCode(200);
    }

    public BillPayResult billPay(int accountId, BigDecimal amount, String payeeName) {
        String payee = """
                {"name":"%s",
                 "address":{"street":"1 Main St","city":"Houston","state":"TX","zipCode":"77001"},
                 "phoneNumber":"7135550100",
                 "accountNumber":12345}
                """.formatted(payeeName);
        Response response = spec()
                .contentType(ContentType.JSON)
                .queryParam("accountId", accountId)
                .queryParam("amount", amount.toPlainString())
                .body(payee)
                .when().post("/billpay")
                .then().log().ifValidationFails().statusCode(200)
                .extract().response();
        JsonPath jp = json(response);
        return new BillPayResult(jp.getString("payeeName"), money(jp.get("amount")), jp.getInt("accountId"));
    }

    // ---------- Transactions ----------

    public List<Transaction> getTransactions(int accountId) {
        Response response = spec()
                .pathParam("accountId", accountId)
                .when().get("/accounts/{accountId}/transactions")
                .then().log().ifValidationFails().statusCode(200)
                .extract().response();
        return toTransactions(json(response));
    }

    public List<Transaction> getTransactionsByAmount(int accountId, BigDecimal amount) {
        Response response = spec()
                .pathParam("accountId", accountId)
                .pathParam("amount", amount.toPlainString())
                .when().get("/accounts/{accountId}/transactions/amount/{amount}")
                .then().log().ifValidationFails().statusCode(200)
                .extract().response();
        return toTransactions(json(response));
    }

    private List<Transaction> toTransactions(JsonPath jp) {
        List<Integer> ids = jp.getList("id");
        List<Integer> accountIds = jp.getList("accountId");
        List<String> types = jp.getList("type");
        List<Object> amounts = jp.getList("amount");
        List<Transaction> result = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            result.add(new Transaction(ids.get(i), accountIds.get(i), types.get(i), money(amounts.get(i))));
        }
        return result;
    }
}