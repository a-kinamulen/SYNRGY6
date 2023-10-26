package com.kinamulen.binarfood.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE user_details SET is_deleted = true, deleted_at = now() WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "user_details")
public class UserDetail extends AuditModel{

    @Column(unique = true)
    private String phoneNumber;
    private String emailAddress;
    private Boolean isVerified;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable=false)
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wallet_id", nullable=false)
    private Wallet wallet;

    @Builder
    public UserDetail(UUID id, boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
                      String phoneNumber, String emailAddress, Boolean isVerified,
                      User user, Wallet wallet) {
        super(id, isDeleted, createdAt, updatedAt, deletedAt);
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.isVerified = isVerified;
        this.user = user;
        this.wallet = wallet;
    }
}
