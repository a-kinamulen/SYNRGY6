package com.kinamulen.binarfood.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE users SET is_deleted = true, deleted_at = now() WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "users")
public class User extends AuditModel{

    @Column(length = 20, nullable = false, unique = true)
    private String username;
    private String password;

    @OneToMany(mappedBy="user") //mappedBy nya nembak di Order
    private Set<Order> orders;

    @OneToOne(mappedBy = "user", optional = false, cascade = CascadeType.ALL)
    private UserDetail userDetail;

    @Builder
    public User(UUID id, boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
                String username, String password,
                Set<Order> orders, UserDetail userDetail) {
        super(id, isDeleted, createdAt, updatedAt, deletedAt);
        this.username = username;
        this.password = password;
        this.orders = orders;
        this.userDetail = userDetail;
    }
}
