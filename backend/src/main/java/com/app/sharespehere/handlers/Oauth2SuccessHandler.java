package com.app.sharespehere.handlers;

import com.app.sharespehere.dto.UserDto;
import com.app.sharespehere.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {

    UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if(authentication instanceof OAuth2AuthenticationToken oAuth2Token){
            OAuth2User principal = oAuth2Token.getPrincipal();
            String email = principal.getAttribute("email");
            if(!userService.userExists(email)){
                userService.saveUser(principal);
                response.sendRedirect("/ui/address");
            }

        }
    }
}
