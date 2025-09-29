package com.example.authdemo.bdd.steps;

import com.example.authdemo.model.User;
import com.example.authdemo.repository.UserRepository;
import com.example.authdemo.service.UserService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration  // ← CHANGE THIS LINE
@SpringBootTest
public class UserStepDefinitions {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Exception lastException;
    private User registeredUser;
    private boolean loginResult;

    @Before
    public void setUp() {
        // Clean up before each scenario
        if (userRepository != null) {
            userRepository.deleteAll();
        }
        lastException = null;
        registeredUser = null;
        loginResult = false;
    }

    @Given("the application is running")
    public void the_application_is_running() {
        assertNotNull(userService, "UserService should be available");
    }

    @Given("no user exists with username {string}")
    public void no_user_exists_with_username(String username) {
        boolean exists = userRepository.existsByUsername(username);
        assertFalse(exists, "Username " + username + " should not exist");
    }

    @Given("a user already exists with username {string}")
    public void a_user_already_exists_with_username(String username) {
        User user = new User(username, "existing@example.com", "encodedPassword");
        userRepository.save(user);
        assertTrue(userRepository.existsByUsername(username));
    }

    @Given("a user already exists with email {string}")
    public void a_user_already_exists_with_email(String email) {
        User user = new User("existinguser", email, "encodedPassword");
        userRepository.save(user);
        assertTrue(userRepository.existsByEmail(email));
    }

    @Given("a user exists with username {string} and password {string}")
    public void a_user_exists_with_username_and_password(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, username + "@example.com", encodedPassword);
        userRepository.save(user);
    }

    @When("I register with username {string}, email {string}, and password {string}")
    public void i_register_with_username_email_and_password(String username, String email, String password) {
        try {
            registeredUser = userService.signup(username, email, password);
        } catch (Exception e) {
            lastException = e;
        }
    }

    @When("I login with username {string} and password {string}")
    public void i_login_with_username_and_password(String username, String password) {
        try {
            loginResult = userService.login(username, password);
        } catch (Exception e) {
            lastException = e;
        }
    }

    @Then("the registration should be successful")
    public void the_registration_should_be_successful() {
        assertNull(lastException, "No exception should be thrown");
        assertNotNull(registeredUser, "User should be registered");
    }

    @Then("the registration should fail with message {string}")
    public void the_registration_should_fail_with_message(String expectedMessage) {
        assertNotNull(lastException, "Exception should be thrown");
        assertEquals(expectedMessage, lastException.getMessage());
    }

    @Then("the login should be successful")
    public void the_login_should_be_successful() {
        assertTrue(loginResult, "Login should be successful");
    }

    @Then("the login should fail")
    public void the_login_should_fail() {
        assertFalse(loginResult, "Login should fail");
    }
}