package com.kinamulen.binarfood.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id //ini menjadikannya primary key
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDateTime orderTime;
    private String destinationAddress;
    private Boolean completed;

//    private UUID userId; //foreign key to user
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @OneToMany(mappedBy="order", cascade = CascadeType.ALL) //mappedBy nya nembak di OrderDetail
    private Set<OrderDetail> ordersDetails;

}
