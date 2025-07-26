package com.Digital.Fuel.Book.Digital.Fuel.Book.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_transaction")
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double liters;
    private double amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status; // PENDING, APPROVED, REJECTED





    @ManyToOne
    @JoinColumn(name = "fueltype_id", nullable = false)
    private FuelBook_Type fuelBook_type;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public enum TransactionStatus {
        PENDING, APPROVED, COMPLETED, REJECTED
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", liters=" + liters +
                ", amount=" + amount +
                ", status=" + status +
                '}';
    }
}