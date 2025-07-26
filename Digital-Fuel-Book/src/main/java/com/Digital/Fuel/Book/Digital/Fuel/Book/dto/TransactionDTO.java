package com.Digital.Fuel.Book.Digital.Fuel.Book.dto;

import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.FuelBook;
import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.Transaction;
import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class TransactionDTO {

    private Long id;

    private double liters;
    private double amount;
    private Transaction.TransactionStatus status;
    private Long userId;
    private Long vehicleId;
    private Long fuelTypeId;
    private Long companyId;
    private String message;
    private Double thresholdAmount;
    private Double pendingTotal;
    private String fuel_type;
}
