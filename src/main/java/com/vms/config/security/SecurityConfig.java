package com.vms.config.security;

import com.vms.config.JwtAuthFilter;
import com.vms.model.enums.AccountType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .cors().and()
                .authorizeHttpRequests(
                        (requests)  -> requests
                                .requestMatchers( "/api/auth/authenticate").permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
//                                .requestMatchers(
//                                        "/api/accounts",
//                                        "/api/auth/register",
//                                        "/api/patterns",
//                                        "/api/form",
//                                        "/api/forms/**",
//                                        "/api/workflows",
//                                        "/api/workflows/**",
//                                        "/api/email",
//                                        "/api/email/**",
//                                        "/api/fields",
//                                        "/api/fields/**"
//                                ).hasAuthority(AccountType.ADMIN.name())
//                                .requestMatchers(HttpMethod.POST,"/api/auth/logout").hasAnyAuthority(AccountType.VENDOR.name(), AccountType.ADMIN.name(), AccountType.APPROVER.name())
//                                .requestMatchers(HttpMethod.GET, "/api/regex").hasAnyAuthority(AccountType.VENDOR.name(), AccountType.ADMIN.name(), AccountType.APPROVER.name())
//                                .requestMatchers(
//                                        "/api/workflows",
//                                        "/api/workflows/**"
//                                ).hasAnyAuthority(AccountType.VENDOR.name())
//                                .requestMatchers(
//                                        "/api/workflows",
//                                        "/api/workflows/**",
//                                        "/api/forms",
//                                        "/api/forms/**",
//                                        "/api/formsubmission",
//                                        "/api/formsubmission/**",
//                                        "/api/fields",
//                                        "/api/fields/**"
//                                ).hasAnyAuthority(AccountType.VENDOR.name(), AccountType.ADMIN.name(), AccountType.APPROVER.name())
                                .requestMatchers("/api/**").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                .and().headers().frameOptions().disable().and()
                .csrf().disable();
        return http.build();
    }
}
