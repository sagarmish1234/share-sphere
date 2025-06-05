package com.app.sharespehere.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserController {


    @GetMapping("/status")
    public String loginStatus(@AuthenticationPrincipal OAuth2User user){
        return "Success "+user.getAttributes().get("name");
    }


}
