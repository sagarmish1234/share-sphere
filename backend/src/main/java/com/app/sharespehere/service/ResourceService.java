package com.app.sharespehere.service;

import com.app.sharespehere.dto.ResourceDto;
import com.app.sharespehere.model.Resource;
import com.app.sharespehere.repository.ResourceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;

@Service
@Slf4j
public class ResourceService {


    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private S3Client s3Client;

    @Value("${aws.bucket}")
    private String bucket;

    public void saveResource(Resource resource) {
        resourceRepository.save(resource);
    }

    public void createResource(ResourceDto resourceDto, MultipartFile imageFile, OAuth2User principal) throws IOException {
        String email = principal.getAttribute("email");
        Resource resource = Resource.builder()
                .name(resourceDto.name())
                .category(categoryService.getCategoryByName(resourceDto.categoryName()))
                .image(this.uploadImage(imageFile, email))
                .description(resourceDto.description())
                .quantity(resourceDto.quantity())
                .isAvailable(resourceDto.quantity() > 0)
                .account(accountService.getUser(email))
                .build();
        this.saveResource(resource);
    }

    public String uploadImage(MultipartFile imageFile,String email) throws IOException {
        String s3ObjectKey = email + "/" + System.currentTimeMillis() + "-" + imageFile.getOriginalFilename();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(s3ObjectKey)
                .build();
        PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(
                imageFile.getInputStream(), imageFile.getSize()));
        log.info("Image upload response {}", putObjectResponse);
        return s3ObjectKey;
    }


}
