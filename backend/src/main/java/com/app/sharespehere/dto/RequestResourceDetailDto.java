package com.app.sharespehere.dto;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RequestResourceDetailDto {

    private RequestResourceDto requestResourceDto;
    private ResourceDto resourceDto;

}
