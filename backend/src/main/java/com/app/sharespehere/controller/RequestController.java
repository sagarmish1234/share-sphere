package com.app.sharespehere.controller;

import com.app.sharespehere.dto.RequestResourceDetailDto;
import com.app.sharespehere.service.RequestResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RequestController {

    @Autowired
    RequestResourceService requestResourceService;

    @PostMapping("/requests")
    public void createRequest(@AuthenticationPrincipal OAuth2User principal, @RequestParam Integer borrowDays, @RequestParam Long resourceId) {
        requestResourceService.createResource(resourceId, principal, borrowDays);
    }

    @PatchMapping("/requests/{id}/approve")
    public void approveRequest(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long id){
        requestResourceService.approveRequest(id,principal);
    }

    @PatchMapping("/requests/{id}/reject")
    public void rejectRequest(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long id){
        requestResourceService.rejectRequest(id,principal);
    }

    @PatchMapping("/requests/{id}/return")
    public void returnRequest(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long id){
        requestResourceService.returnRequest(id,principal);
    }

    @GetMapping("/requests/owner")
    public List<RequestResourceDetailDto> getOwnerRequests(@AuthenticationPrincipal OAuth2User principal){
        return requestResourceService.getOwnerRequests(principal);
    }

    @GetMapping("/requests/borrower")
    public List<RequestResourceDetailDto> getBorrowerRequests(@AuthenticationPrincipal OAuth2User principal){
        return requestResourceService.getBorrowerRequests(principal);
    }
}
