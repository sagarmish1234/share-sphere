package com.app.sharespehere.controller;

import com.app.sharespehere.dto.AddressAndPhoneDto;
import com.app.sharespehere.dto.AccountDto;
import com.app.sharespehere.service.AccountService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AccountController {

    AccountService accountService;

    @GetMapping("/status")
    public String loginStatus(@AuthenticationPrincipal OAuth2User user) {
        return "Success " + user.getAttributes().get("name");
    }

    @PostMapping("/address")
    public void saveAddress(@AuthenticationPrincipal OAuth2User user, @RequestBody AddressAndPhoneDto addressAndPhoneDto) {

        accountService.saveAddressAndPhone(addressAndPhoneDto, user);
    }

    @GetMapping("/profile")
    public AccountDto fetchUserProfile(@AuthenticationPrincipal OAuth2User user) {
        return accountService.getProfile(user);
    }


}
