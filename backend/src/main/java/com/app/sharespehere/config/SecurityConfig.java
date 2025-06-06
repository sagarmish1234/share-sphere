package com.app.sharespehere.config;

import com.app.sharespehere.filter.AddressCheckFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import java.util.LinkedList;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    AuthenticationSuccessHandler successHandler;

    @Autowired
    AddressCheckFilter addressCheckFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
//                .addFilterAfter(addressCheckFilter, SecurityContextPersistenceFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(getPublicUrls()).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/ui/login")
                        .successHandler(successHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );



        return http.build();
    }

    public String[] getPublicUrls() {
        LinkedList<String> publicUrls = new LinkedList<>();
        publicUrls.add("/**.html");
        publicUrls.add("/assets/**");
        publicUrls.add("/**.svg");
        publicUrls.add("/logout");
        publicUrls.add( "/error");
        publicUrls.add("/");

        return publicUrls.toArray(String[]::new);
    }


}
