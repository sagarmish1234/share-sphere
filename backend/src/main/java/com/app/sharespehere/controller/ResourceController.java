package com.app.sharespehere.controller;


import com.app.sharespehere.dto.ResourceDto;
import com.app.sharespehere.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class ResourceController {

    @Autowired
    ResourceService resourceService;


    @PostMapping("/resources")
    public void createResource(@RequestParam MultipartFile file, @RequestParam ResourceDto body, @AuthenticationPrincipal OAuth2User user) throws IOException {

        resourceService.createResource(body, file, user);

    }

    @GetMapping("/resources/{id}")
    public ResourceDto fetchResource(@PathVariable Long id) {
        return resourceService.getResource(id);
    }

    @PutMapping("/resources/{id}")
    public void updateResource(@RequestParam MultipartFile file, @RequestParam ResourceDto body, @AuthenticationPrincipal OAuth2User user, @PathVariable Long id) throws IOException {

        resourceService.updateResource(body, file, user, id);

    }

    @DeleteMapping("/resources/{id}")
    public void deleteResource(@PathVariable Long id, @AuthenticationPrincipal OAuth2User user) {
        resourceService.deleteResource(id, user);
    }


}
