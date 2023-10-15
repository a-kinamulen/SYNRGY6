package com.kinamulen.binarfood.entity;

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
@Table(name = "wallets")
public class Wallet {

    @Id //ini menjadikannya primary key
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Double balance;
    @Enumerated(EnumType.STRING)
    private WalletType type;

    @OneToOne(mappedBy = "wallet", cascade = CascadeType.ALL)
    private UserDetail userDetail;

    @OneToOne(mappedBy = "wallet", cascade = CascadeType.ALL)
    private MerchantDetail merchantDetail;
}
