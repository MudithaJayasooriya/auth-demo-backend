package com.example.authdemo.bdd.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

// REMOVED @CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
public class SpringIntegrationTest {
    // This class provides additional Spring configuration
    // but should NOT have @CucumberContextConfiguration
}