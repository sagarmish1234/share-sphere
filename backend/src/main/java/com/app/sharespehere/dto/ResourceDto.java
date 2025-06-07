package com.app.sharespehere.dto;

import lombok.Builder;

@Builder
public record ResourceDto(String name,String description,Integer quantity, String categoryName, String imageUrl) {
}
