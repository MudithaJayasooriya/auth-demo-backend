Feature: User Authentication
  As a user, I want to register and login to the system

  Background:
    Given the application is running

  Scenario: Successful user registration
    Given no user exists with username "john_doe"
    When I register with username "john_doe", email "john@example.com", and password "SecurePass123"
    Then the registration should be successful

  Scenario: Registration with existing username
    Given a user already exists with username "existinguser"
    When I register with username "existinguser", email "new@example.com", and password "SecurePass123"
    Then the registration should fail with message "Username already exists"

  Scenario: Successful login
    Given a user exists with username "validuser" and password "ValidPass123"
    When I login with username "validuser" and password "ValidPass123"
    Then the login should be successful

  Scenario: Failed login with wrong password
    Given a user exists with username "validuser" and password "ValidPass123"
    When I login with username "validuser" and password "wrongpassword"
    Then the login should fail