package com.aliparmar.sdet.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Runner class that bridges Cucumber and TestNG.
 * Tells Cucumber where to find feature files and step definitions.
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.aliparmar.sdet.stepdefs"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber-html-report.html",
                "json:target/cucumber-reports/cucumber-results.json"
        },
        monochrome = true
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {
}