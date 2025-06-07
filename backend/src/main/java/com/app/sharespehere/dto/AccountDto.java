package com.app.sharespehere.dto;

import lombok.Builder;

@Builder
public record AccountDto(String email, String name, String city, String state, String address, String phone) {
}
