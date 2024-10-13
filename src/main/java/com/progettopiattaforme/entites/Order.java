package com.progettopiattaforme.entites;




import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.List;


@Data
@Entity
@Table(name = "order", schema = "store")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "purchase_time")
    private Date purchaseTime;


    @ManyToOne
    @JoinColumn(name = "buyer")
    private User buyer;


    @OneToMany(mappedBy = "order", cascade = CascadeType.MERGE)
    private List<ProductInPurchase> productsInPurchase;
}

    // preferiti(user) =>> prodotti





