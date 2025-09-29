package com.example.authdemo.selenium.base.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.UUID;

public class SignupTest {

    @Test
    public void testSignupDirect() {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("http://localhost:5173/sign_up");

        String uniqueEmail = "test" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";

        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        WebElement emailInput = driver.findElement(By.id("email"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement signupButton = driver.findElement(By.cssSelector(".auth-button"));

        nameInput.sendKeys("abc12");
        emailInput.sendKeys(uniqueEmail);
        passwordInput.sendKeys("abc12345");

        wait.until(ExpectedConditions.elementToBeClickable(signupButton)).click();
        wait.until(ExpectedConditions.urlContains("/log_in"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/log_in"),
                "Should redirect to login page after signup");

        driver.quit();
    }


}
