package com.example.authdemo.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.example.authdemo.bdd.steps"}, // Remove "com.example.authdemo.bdd.config"
        plugin = {
                "pretty",
                "summary", // Adds test summary to console
                "html:target/cucumber-reports.html",
                "json:target/cucumber.json"
        },
        monochrome = true
)
public class CucumberTest {
        // This uses JUnit 4 which is more reliable for Cucumber
}