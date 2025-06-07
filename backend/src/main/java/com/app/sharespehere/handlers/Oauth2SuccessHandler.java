package com.app.sharespehere.handlers;

import com.app.sharespehere.service.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Oauth2SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    AccountService accountService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if(authentication instanceof OAuth2AuthenticationToken oAuth2Token){
            OAuth2User principal = oAuth2Token.getPrincipal();
            String email = principal.getAttribute("email");
            if(!accountService.userExists(email)){
                accountService.saveAccount(principal);
            }

        }
        super.onAuthenticationSuccess(request,response,authentication);
    }
}
