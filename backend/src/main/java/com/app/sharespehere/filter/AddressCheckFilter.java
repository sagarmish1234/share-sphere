package com.app.sharespehere.filter;

import com.app.sharespehere.model.User;
import com.app.sharespehere.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class AddressCheckFilter extends OncePerRequestFilter {

    UserService userService;


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
            Optional<User> user = userService.getUser(email);

            if (user.isPresent() && ObjectUtils.isEmpty(user.get().getAddress())) {
                response.sendRedirect("/address");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
