package com.app.sharespehere.converter;

import com.app.sharespehere.dto.ResourceDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ResourceDtoConverter implements Converter<String, ResourceDto> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ResourceDto convert(String source) {
        try {
            return objectMapper.readValue(source, ResourceDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
