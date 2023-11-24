package com.kinamulen.securityservice.entity;

import com.kinamulen.securityservice.enums.UserType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_credentials")
public class UserCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private UUID binarfoodId;
    @Enumerated(EnumType.STRING)
    private UserType type;
}
