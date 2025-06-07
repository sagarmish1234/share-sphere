package com.app.sharespehere.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Resource extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String name;

    @Column(name = "image")
    String image;

    String description;

    String quantity;

    @ManyToOne
    Category category;

    boolean isAvailable;

    @ManyToOne(fetch = FetchType.LAZY)
    Account account;


}
