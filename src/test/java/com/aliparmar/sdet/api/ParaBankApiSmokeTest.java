package com.aliparmar.sdet.api;

import com.aliparmar.sdet.base.BaseApiTest;
import com.aliparmar.sdet.utils.ConfigReader;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

/**
 * Smoke tests for the ParaBank REST API.
 * Verifies the API is reachable and core endpoints return expected data.
 * Paths verified against the ParaBank service interface (parasoft/parabank on GitHub).
 */

public class ParaBankApiSmokeTest extends BaseApiTest {

    private static final String CUSTOMER_ID = "12212";

    @Test(groups = {"smoke", "api"}, description = "Login endpoint returns 200 and a customer id for valid credentials")
    public void verifyValidLoginReturnsCustomer() {
        String username = ConfigReader.get("test.username");
        String password = ConfigReader.get("test.password");

        given()
                .when()
                .get("/login/" + username + "/" + password)
                .then()
                .statusCode(200)
                .body("id", equalTo(12212));
    }

    @Test(groups = {"smoke", "api"}, description = "Customer accounts endpoint returns a non-empty list")
    public void verifyCustomerAccountsEndpoint() {
        given()
                .when()
                .get("/customers/" + CUSTOMER_ID + "/accounts")
                .then()
                .statusCode(200)
                .body("size()", notNullValue())
                .body(matchesJsonSchemaInClasspath("schemas/account-schema.json"));
    }

    @Test(groups = {"sanity", "api"}, description = "Login endpoint rejects invalid credentials")
    public void verifyInvalidLoginIsRejected() {
        given()
                .when()
                .get("/login/john/wrongPassword123")
                .then()
                .statusCode(equalTo(400));
    }
    @Test(groups = {"smoke", "api"}, description = "Transactions endpoint returns data for a dynamically retrieved account")
    public void verifyAccountTransactionsEndpoint() {

        // Step 1: get the accounts list for customer 12212, extract the first account's id
        int accountId = given()
                .when()
                .get("/customers/" + CUSTOMER_ID + "/accounts")
                .then()
                .statusCode(200)
                .extract()
                .path("[0].id");

        // Step 2: use that account id to fetch its transactions
        given()
                .when()
                .get("/accounts/" + accountId + "/transactions")
                .then()
                .statusCode(200)
                .body("size()", notNullValue());
    }

    @Test(groups = {"sanity", "api"}, description = "Accounts endpoint rejects a nonexistent customer id")
    public void verifyInvalidCustomerIdIsRejected() {
        given()
                .when()
                .get("/customers/99999999/accounts")
                .then()
                .statusCode(400)
                .body(containsString("Could not find customer #99999999"));
    }

}