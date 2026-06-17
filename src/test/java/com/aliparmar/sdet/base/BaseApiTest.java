package com.aliparmar.sdet.base;

import com.aliparmar.sdet.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;

/**
 * Base class for all REST API test classes.
 * Sets the API base URI and default content type from config.
 */
public class BaseApiTest {

    @BeforeClass(alwaysRun = true)
    public void setUpApi() {
        RestAssured.baseURI = ConfigReader.get("api.base.url");
        RestAssured.requestSpecification = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON);
    }
}