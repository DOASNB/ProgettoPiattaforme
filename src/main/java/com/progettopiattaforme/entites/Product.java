package com.progettopiattaforme.entites;





import com.fasterxml.jackson.annotation.JsonIgnore;
import com.progettopiattaforme.support.Category;
import lombok.*;
import jakarta.persistence.*;
import java.util.List;



@Data
@ToString
@Entity
@Table(name = "product", schema = "store")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "name", nullable = true, length = 50)
    private String name;

    @Basic
    @Column(name = "code", nullable = true, length = 70)
    private String barCode;

    @Basic
    @Column(name = "description", nullable = true, length = 500)
    private String description;

    @Basic
    @Column(name = "price", nullable = true)
    private float price;

    @Basic
    @Column(name = "quantity", nullable = true)
    private int quantity;

    @Basic
    @Column(name = "image_name",nullable = true)
    private String imageName;

    @Version
    @Column
    private int version;

    @OneToMany(targetEntity = ProductInPurchase.class, mappedBy = "product", cascade = CascadeType.MERGE)
    @JsonIgnore
    @ToString.Exclude
    private List<ProductInPurchase> productsInPurchase;





}

