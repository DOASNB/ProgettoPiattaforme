package com.progettopiattaforme.entites;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import jakarta.persistence.*;
import java.util.List;
import java.util.Set;


@Data
@Entity
@Table(name = "user", schema = "store")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "firstname", nullable = true, length = 50)
    private String firstName;

    @Basic
    @Column(name = "lastname", nullable = true, length = 50)
    private String lastName;

    @Basic
    @Column(name = "phone_number", nullable = true, length = 20)
    private String telephoneNumber;

    @Basic
    @Column(name = "email", nullable = true, length = 90)
    private String email;

    @Basic
    @Column(name = "address", nullable = true, length = 150)
    private String address;

    @JsonIgnore
    @ManyToMany(targetEntity = Product.class,cascade = CascadeType.ALL)
    private Set<Product> favorites;





}
