package com.kinamulen.binarfood.entity;

import com.kinamulen.binarfood.enums.UserType;
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
@SQLDelete(sql = "UPDATE wallets SET is_deleted = true, deleted_at = now() WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "wallets")
public class Wallet extends AuditModel{

    private Double balance;
    @Enumerated(EnumType.STRING)
    private UserType type;

    @OneToOne(mappedBy = "wallet", cascade = CascadeType.ALL)
    private UserDetail userDetail;

    @OneToOne(mappedBy = "wallet", cascade = CascadeType.ALL)
    private MerchantDetail merchantDetail;

    @Builder
    public Wallet(UUID id, boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
                  Double balance, UserType type,
                  UserDetail userDetail, MerchantDetail merchantDetail) {
        super(id, isDeleted, createdAt, updatedAt, deletedAt);
        this.balance = balance;
        this.type = type;
        this.userDetail = userDetail;
        this.merchantDetail = merchantDetail;
    }
}
