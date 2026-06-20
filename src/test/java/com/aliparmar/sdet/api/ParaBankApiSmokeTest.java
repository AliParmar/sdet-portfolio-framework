package com.aliparmar.sdet.api;

import com.aliparmar.sdet.base.BaseApiTest;
import com.aliparmar.sdet.utils.ConfigReader;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

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
}