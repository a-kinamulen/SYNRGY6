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
@SQLDelete(sql = "UPDATE merchants SET is_deleted = true, deleted_at = now() WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "merchants")
public class Merchant extends AuditModel {

    @Column(length = 30, unique = true) //bisa jadi layer terakhir validasi [bad practice]
    private String merchantName;
    private String password;
    private Boolean open;

    @OneToMany(mappedBy="merchant",fetch = FetchType.EAGER) //mappedBy nya nembak di Product
    private Set<Product> products;

    @OneToOne(mappedBy = "merchant", optional = false, cascade = CascadeType.ALL)
    private MerchantDetail merchantDetail;

    @Builder
    public Merchant(UUID id, boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
                    String merchantName, String password,  Boolean open,
                    Set<Product> products,MerchantDetail merchantDetail) {
        super(id, isDeleted, createdAt, updatedAt, deletedAt);
        this.merchantName = merchantName;
        this.password = password;
        this.open = open;
        this.products = products;
        this.merchantDetail = merchantDetail;
    }
}
