package com.Digital.Fuel.Book.Digital.Fuel.Book.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "tbl_fuel_book")
@Data
public class FuelBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "Initial_balance")
    private double initialBalance;

    @Column(name = "current_balance")
    private double currentBalance;

    @Column(name = "threshold_amount")
    private double thresholdAmount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Override
    public String toString() {
        return "FuelBook{" +
                "id=" + id +
                ", initialBalance=" + initialBalance +
                ", currentBalance=" + currentBalance +
                ", thresholdAmount=" + thresholdAmount +
                '}';
    }


}
