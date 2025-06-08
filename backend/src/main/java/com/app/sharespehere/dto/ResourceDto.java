package com.app.sharespehere.dto;

import lombok.Builder;

@Builder
public record ResourceDto(Long id,String name,String description,Integer quantity, String categoryName, String imageUrl) {
}
