package com.app.sharespehere.controller;

import com.app.sharespehere.dto.AddressAndPhoneDto;
import com.app.sharespehere.dto.UserDto;
import com.app.sharespehere.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    UserService userService;

    @GetMapping("/status")
    public String loginStatus(@AuthenticationPrincipal OAuth2User user) {
        return "Success " + user.getAttributes().get("name");
    }

    @PostMapping("/address")
    public void saveAddress(@AuthenticationPrincipal OAuth2User user, @RequestBody AddressAndPhoneDto addressAndPhoneDto) {

        userService.saveAddressAndPhone(addressAndPhoneDto, user);
    }

    @GetMapping("/profile")
    public UserDto fetchUserProfile(@AuthenticationPrincipal OAuth2User user) {
        return userService.getProfile(user);
    }


}
