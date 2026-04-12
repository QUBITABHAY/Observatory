package com.qubitabhay.observatory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/", "/dashboard", "/alerts", "/logs", "/hosts", "/metrics", "/traces")
                        .hasAnyRole("ADMIN", "DEVELOPER", "OPERATOR")
                        .requestMatchers(HttpMethod.POST, "/api/metrics", "/api/logs", "/api/traces", "/api/spans")
                        .hasAnyRole("ADMIN", "DEVELOPER")
                        .requestMatchers(HttpMethod.GET, "/api/**")
                        .hasAnyRole("ADMIN", "DEVELOPER", "OPERATOR")
                        .requestMatchers("/api/users/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/hosts", "/api/services", "/api/alert-rules/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/alerts/**")
                        .hasAnyRole("ADMIN", "OPERATOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/alert-rules/**")
                        .hasRole("ADMIN")
                        .requestMatchers("/api/observe/**")
                        .hasAnyRole("ADMIN", "OPERATOR")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
