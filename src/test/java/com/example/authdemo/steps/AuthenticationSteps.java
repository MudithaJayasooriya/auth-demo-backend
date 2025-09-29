package com.example.authdemo.steps;

import com.example.authdemo.model.User;
import com.example.authdemo.repository.UserRepository;
import com.example.authdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthenticationSteps {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private String username;
    private String email;
    private String password;
    private boolean loginResult;
    private User signupResult;

    @io.cucumber.java.en.Given("I am a new user with username {string} email {string} and password {string}")
    public void i_am_a_new_user(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        userRepository.deleteAll(); // clean slate
    }

    @io.cucumber.java.en.When("I send a signup request")
    public void i_send_a_signup_request() {
        signupResult = userService.signup(username, email, password);
    }

    @io.cucumber.java.en.Then("I should receive a successful signup response")
    public void i_should_receive_a_successful_signup_response() {
        assertNotNull(signupResult);
        assertEquals(email, signupResult.getEmail());
    }

    @io.cucumber.java.en.Given("an existing user with username {string} and password {string}")
    public void an_existing_user(String username, String password) {
        this.username = username;
        this.password = password;
        this.email = username + "@example.com";
        userRepository.deleteAll();
        userService.signup(username, email, password);
    }

    @io.cucumber.java.en.When("I send a login request")
    public void i_send_a_login_request() {
        loginResult = userService.login(username, password);
    }

    @io.cucumber.java.en.When("I send a login request with password {string}")
    public void i_send_a_login_request_with_password(String wrongPassword) {
        loginResult = userService.login(username, wrongPassword);
    }

    @io.cucumber.java.en.Then("I should receive a successful login response")
    public void i_should_receive_a_successful_login_response() {
        assertTrue(loginResult);
    }

    @io.cucumber.java.en.Then("I should receive a login failure response")
    public void i_should_receive_a_login_failure_response() {
        assertFalse(loginResult);
    }
}

