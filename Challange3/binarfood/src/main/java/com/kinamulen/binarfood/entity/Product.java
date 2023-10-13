package com.kinamulen.binarfood.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {

    @Id //ini menjadikannya primary key
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String productName;
    private Double price;

//    private UUID merchantId; //foreign key to merchant
    @ManyToOne
    @JoinColumn(name="merchant_id", nullable=false)
    private Merchant merchant;

    @OneToMany(mappedBy="product", cascade = CascadeType.ALL) //mappedBy nya nembak di OrderDetail
    private Set<OrderDetail> ordersDetails;
}
