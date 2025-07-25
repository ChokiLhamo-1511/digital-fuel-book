package com.Digital.Fuel.Book.Digital.Fuel.Book.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name="tbl_fuelbook_type")
@Data
public class FuelBook_Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fuel_type;


}
