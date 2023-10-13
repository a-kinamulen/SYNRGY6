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
@Table(name = "merchants")
public class Merchant {

    @Id //ini menjadikannya primary key
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(length = 30) //bisa jadi layer terakhir validasi [bad practice]
    private String merchantName;
    private Boolean open;

    @OneToMany(mappedBy="merchant",fetch = FetchType.EAGER) //mappedBy nya nembak di Product
    private Set<Product> products;

    @OneToOne(mappedBy = "merchant", optional = false, cascade = CascadeType.ALL)
    private MerchantDetail merchantDetail;
}
