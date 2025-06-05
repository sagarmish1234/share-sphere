package com.app.sharespehere.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.LinkedList;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(getPublicUrls()).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/index.html")
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }

    public String[] getPublicUrls() {
        LinkedList<String> publicUrls = new LinkedList<>();
        publicUrls.add("/index.html");
        publicUrls.add("/ui/login");
        publicUrls.add("/api/v1/login");
        publicUrls.add("/assets/**");
        publicUrls.add("/**.svg");

        publicUrls.add( "/error");
        publicUrls.add("/");

        return publicUrls.toArray(String[]::new);
    }


}
