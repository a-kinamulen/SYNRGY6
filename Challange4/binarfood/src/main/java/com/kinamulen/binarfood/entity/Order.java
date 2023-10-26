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
@SQLDelete(sql = "UPDATE orders SET is_deleted = true, deleted_at = now() WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "orders")
public class Order extends AuditModel{

    private LocalDateTime orderTime;
    private String destinationAddress;
    private Boolean completed;

//    private UUID userId; //foreign key to user
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @OneToMany(mappedBy="order", cascade = CascadeType.ALL) //mappedBy nya nembak di OrderDetail
    private Set<OrderDetail> ordersDetails;

    @Builder
    public Order(UUID id, boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, LocalDateTime orderTime,
                 String destinationAddress, Boolean completed,
                 User user, Set<OrderDetail> ordersDetails) {
        super(id, isDeleted, createdAt, updatedAt, deletedAt);
        this.orderTime = orderTime;
        this.destinationAddress = destinationAddress;
        this.completed = completed;
        this.user = user;
        this.ordersDetails = ordersDetails;
    }
}
