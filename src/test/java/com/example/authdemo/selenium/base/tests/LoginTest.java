package com.example.authdemo.selenium.base.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;

public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String baseUrl = "http://localhost:5173";

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // ✅ Use the correct route from your React app
        driver.get(baseUrl + "/log_in");
    }
    @Test
    public void testSuccessfulLogin() {
        WebElement usernameInput = driver.findElement(By.id("username"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-btn"));

        usernameInput.sendKeys("abc12");   // replace with real test user
        passwordInput.sendKeys("abc12345"); // replace with real password
        loginButton.click();

        wait.until(ExpectedConditions.urlToBe(baseUrl + "/"));
        Assert.assertEquals(driver.getCurrentUrl(), baseUrl + "/");
    }



    @Test
    public void testNavigationToSignUp() {
        WebElement signUpLink = driver.findElement(By.linkText("SIGN UP"));
        signUpLink.click();

        // ✅ Match your React route
        wait.until(ExpectedConditions.urlContains("/sign_up"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/sign_up"));
    }

}
