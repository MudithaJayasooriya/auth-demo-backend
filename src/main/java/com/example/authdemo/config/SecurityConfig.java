package com.example.authdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * This bean configures the security filter chain.
     * It disables CSRF protection and authorizes all requests to the /api/auth/** endpoints.
     * This is necessary because Spring Security's default behavior will block unauthenticated
     * POST requests, which your login and signup endpoints are.
     * @param http The HttpSecurity object to configure.
     * @return A configured SecurityFilterChain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for API endpoints
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**").permitAll() // Allow all requests to auth endpoints
                        .anyRequest().authenticated() // Require authentication for all other requests
                );
        return http.build();
    }

    /**
     * This bean provides a PasswordEncoder for hashing and verifying passwords.
     * BCrypt is a strong, industry-standard algorithm.
     * The UserService requires this bean to be present.
     * @return A BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
