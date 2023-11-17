package com.kinamulen.binarfood.entity;

import com.kinamulen.binarfood.enums.MerchantType;
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
@SQLDelete(sql = "UPDATE merchant_details SET is_deleted = true, deleted_at = now() WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "merchant_details")
public class MerchantDetail extends AuditModel{

    private String phoneNumber;
    private String merchantLocation;

    @Enumerated(EnumType.STRING)
    private MerchantType merchantType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "merchant_id", nullable=false)
    private Merchant merchant;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wallet_id", nullable=false)
    private Wallet wallet;

    @Builder
    public MerchantDetail(UUID id, boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
                          String phoneNumber, String merchantLocation,
                          MerchantType merchantType, Merchant merchant, Wallet wallet) {
        super(id, isDeleted, createdAt, updatedAt, deletedAt);
        this.phoneNumber = phoneNumber;
        this.merchantLocation = merchantLocation;
        this.merchantType = merchantType;
        this.merchant = merchant;
        this.wallet = wallet;
    }
}
