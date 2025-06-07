package com.app.sharespehere.service;

import com.app.sharespehere.dto.ResourceDto;
import com.app.sharespehere.exception.*;
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
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;

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

    @Autowired
    private S3Presigner s3Presigner;

    @Value("${aws.bucket}")
    private String bucket;

    public void saveResource(Resource resource) {
        resourceRepository.save(resource);
    }

    public void createResource(ResourceDto resourceDto, MultipartFile imageFile, OAuth2User principal) {
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

    public String uploadImage(MultipartFile imageFile, String email) {
        String s3ObjectKey = email + "/" + System.currentTimeMillis() + "-" + imageFile.getOriginalFilename();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(s3ObjectKey)
                .build();
        PutObjectResponse putObjectResponse;
        try {
            putObjectResponse = s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(
                    imageFile.getInputStream(), imageFile.getSize()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("Image upload response {}", putObjectResponse);
        return s3ObjectKey;
    }

    public Resource getResourceById(Long resourceId) {
        return resourceRepository.findById(resourceId).orElseThrow(() -> new ResourceNotFoundException(resourceId.toString()));
    }

    public ResourceDto getResource(Long resourceId) {
        Resource resource = this.getResourceById(resourceId);
        return ResourceDto.builder()
                .name(resource.getName())
                .description(resource.getDescription())
                .categoryName(resource.getCategory().getName())
                .imageUrl(this.getImageUrl(resource.getImage()))
                .quantity(resource.getQuantity())
                .build();
    }

    public String getImageUrl(String s3ObjectKey) {
        log.info("Bucket name {}", bucket);
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60)) // URL valid for 60 minutes
                .getObjectRequest(builder -> builder.bucket(bucket).key(bucket + "/" + s3ObjectKey))
                .build();
        return s3Presigner.presignGetObject(presignRequest).url().toString();

    }


    public void updateResource(ResourceDto res, MultipartFile imageFile, OAuth2User principal, Long resourceId) {
        Resource resource = this.getResourceById(resourceId);
        String email = principal.getAttribute("email");
        if (!resource.getAccount().getEmail().equals(email))
            throw new UpdateDeniedException();
        resource.setQuantity(res.quantity());
        resource.setName(res.name());
        resource.setDescription(res.description());
        resource.setImage(this.uploadImage(imageFile, email));
        resource.setAvailable(res.quantity() > 0);
        this.saveResource(resource);
    }

    public void deleteResource(Long resourceId, OAuth2User principal) {
        Resource resource = null;
        try {
            resource = this.getResourceById(resourceId);

        } catch (NotFoundException e) {
            log.info("Resource with id {}", resourceId);
            return;
        }
        String email = principal.getAttribute("email");
        if (!resource.getAccount().getEmail().equals(email))
            throw new DeleteDeniedException();
        resourceRepository.deleteById(resourceId);
    }


}
