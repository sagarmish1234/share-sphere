package com.app.sharespehere.mapper;


import com.app.sharespehere.dto.RequestResourceDto;
import com.app.sharespehere.dto.ResourceDto;
import com.app.sharespehere.model.RequestResource;
import com.app.sharespehere.model.Resource;
import com.app.sharespehere.service.ResourceService;

public class ModelMapper {


    public static ResourceDto maptoResourceDto(Resource resource, ResourceService resourceService) {
        return ResourceDto.builder()
                .id(resource.getId())
                .name(resource.getName())
                .description(resource.getDescription())
                .categoryName(resource.getCategory().getName())
                .imageUrl(resourceService.getImageUrl(resource.getImage()))
                .quantity(resource.getQuantity())
                .build();
    }

    public static RequestResourceDto mapToRequestResourceDto(RequestResource requestResource){
        return RequestResourceDto.builder()
                .resourceId(requestResource.getResource().getId())
                .accountId(requestResource.getOwner().getId())
                .borrowDate(requestResource.getBorrowDate())
                .returnDate(requestResource.getReturnDate())
                .status(requestResource.getStatus())
                .build();
    }


}
