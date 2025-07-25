package com.Digital.Fuel.Book.Digital.Fuel.Book.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tbl_vehicle")
@Data
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "registration_number", nullable = false, unique = true)
    private String registrationNumber;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "fuelbook_id")
    private FuelBook fuelBook;

    @ManyToOne
    @JoinColumn(name = "fuelbook_type_id")
    private FuelBook_Type fuelBook_type;

    @OneToMany(mappedBy = "vehicle")
    private Set<User> drivers = new HashSet<>();






}

