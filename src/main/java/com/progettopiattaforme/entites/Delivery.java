package com.progettopiattaforme.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@Entity
@ToString
@Table(name="delivery",schema = "store")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable = false)
    private int id;

    @Basic
    @Column(name="starting_date",nullable = false)
    private Date starting_date;

    @Basic
    @Column(name="delivery_date",nullable = false)
    private Date delivery_date;

    @Basic
    @Column(name="delivered",nullable = false)
    private boolean delivered = false;

    @Basic
    @Column(name="shipped",nullable = false)
    private boolean shipped = false;

    @Basic
    @Column(name="address",nullable = false)
    private String address;

    @OneToOne
    @JoinColumn(name = "related_purchase")
    @JsonIgnore
    @ToString.Exclude
    private Purchase purchase;





}




