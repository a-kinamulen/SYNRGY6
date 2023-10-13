package com.kinamulen.binarfood.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id //ini menjadikannya primary key
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 20)
    private String username;
    private String password;

    @OneToMany(mappedBy="user") //mappedBy nya nembak di Order
    private Set<Order> orders;

    @OneToOne(mappedBy = "user", optional = false, cascade = CascadeType.ALL)
    private UserDetail userDetail;
}
