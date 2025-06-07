package com.app.sharespehere.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@Setter
@Builder
public class Account extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Email
    String email;

    @Length(min = 3, max = 30)
    String name;

    String city;

    String state;

    String address;

    @Pattern(regexp="(^$|[0-9]{10})")
    String phone;

}
