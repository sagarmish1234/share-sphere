package com.app.sharespehere.filter;

import com.app.sharespehere.model.Account;
import com.app.sharespehere.service.AccountService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

//@Component
@Slf4j
public class AddressCheckFilter extends OncePerRequestFilter {

    AccountService accountService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        log.info("requestURI {}",requestURI);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Skip filter if user is not authenticated
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            filterChain.doFilter(request, response);
            return;
        }
        if(requestURI.equals("/ui/address") || requestURI.equals("/api/v1/address") ){
            filterChain.doFilter(request,response);
        }
        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            String email = oauthToken.getPrincipal().getAttribute("email");
            Account user = accountService.getUser(email);

            if (ObjectUtils.isEmpty(user.getAddress())) {
                response.sendRedirect("/address");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
