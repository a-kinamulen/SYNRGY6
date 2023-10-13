package com.kinamulen.binarfood.entity;

import com.kinamulen.binarfood.enums.MerchantType;
import com.kinamulen.binarfood.enums.WalletType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "merchant_details")
public class MerchantDetail {

    @Id //ini menjadikannya primary key
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
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
}
