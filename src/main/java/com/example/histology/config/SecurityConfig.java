package com.example.histology.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.example.histology.service.AppUserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AppUserDetailsService appUserDetailsService) throws Exception {
        System.out.println("[DEBUG] SecurityConfig loaded: SecurityFilterChain bean initialized");
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/register", "/register/**", "/login", "/login/**", "/css/**", "/js/**", "/webjars/**").permitAll()
                .anyRequest().permitAll()
            )
            .userDetailsService(appUserDetailsService)
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable())  // Disable CSRF for simplicity in development
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())  // Allow iframes from the same origin
            );
        
        return http.build();
    }
}
