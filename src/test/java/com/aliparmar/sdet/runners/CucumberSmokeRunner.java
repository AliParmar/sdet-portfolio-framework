package com.aliparmar.sdet.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.aliparmar.sdet.stepdefs"},
        tags = "@smoke",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber-smoke-report.html",
                "json:target/cucumber-reports/cucumber-smoke-results.json"
        },
        monochrome = true
)
public class CucumberSmokeRunner extends AbstractTestNGCucumberTests {
}
