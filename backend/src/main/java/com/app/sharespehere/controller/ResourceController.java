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


    @PostMapping("/resource")
    public void createResource(@RequestParam MultipartFile file, @RequestParam ResourceDto body, @AuthenticationPrincipal OAuth2User user) throws IOException {

        resourceService.createResource(body,file,user);

    }

}
