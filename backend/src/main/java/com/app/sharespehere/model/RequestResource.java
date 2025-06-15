package com.app.sharespehere.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestResource extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Resource resource;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Account borrower;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Account owner;

    @Column(nullable = false)
    private Integer borrowDays;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    private Date borrowDate;

    private Date returnDate;

}
