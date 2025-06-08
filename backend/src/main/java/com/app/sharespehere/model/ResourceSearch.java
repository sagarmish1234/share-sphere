package com.app.sharespehere.model;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "resource")
@Data
@Builder
public class ResourceSearch {

    @Id
    Long id;
    @Field(type = FieldType.Text)
    String name;
    @Field(type = FieldType.Text)
    String description;
    @Field(type = FieldType.Text)
    String category;

}
