
package com.example.unisphere.config;

import com.google.firebase.FirebaseApp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.unisphere.security.FirebaseTokenFilter;
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            FirebaseApp firebaseApp
    ) throws Exception {

        FirebaseTokenFilter firebaseTokenFilter =
                new FirebaseTokenFilter(firebaseApp);

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        // Public event browsing
                        .requestMatchers(
                                HttpMethod.GET,
                                "/events",
                                "/events/{eventId}",
                                "/events/search",
                                "/events/category",
                                "/events/date",
                                "/events/upcoming",
                                "/events/date-range"
                        ).permitAll()

                        // Public club browsing
                        .requestMatchers(
                                HttpMethod.GET,
                                "/clubs",
                                "/clubs/{clubId}",
                                "/clubs/search",
                                "/clubs/filter"
                        ).permitAll()

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        firebaseTokenFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling(exceptions ->
                        exceptions.authenticationEntryPoint(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
                        )
                );

        return http.build();
    }
}