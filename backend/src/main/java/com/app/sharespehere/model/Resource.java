package com.app.sharespehere.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String name;

    @Column(name = "image")
    String image;

    String description;

    Integer quantity;

    @ManyToOne
    Category category;

    boolean isAvailable;

    @ManyToOne(fetch = FetchType.LAZY)
    Account account;


}
