package com.Digital.Fuel.Book.Digital.Fuel.Book.service.impl;

import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.TransactionDTO;
import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.*;
import com.Digital.Fuel.Book.Digital.Fuel.Book.exception.ThresholdNotMetException;
import com.Digital.Fuel.Book.Digital.Fuel.Book.repo.*;
import com.Digital.Fuel.Book.Digital.Fuel.Book.exception.InsufficientBalanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private FuelBookRepo fuelBookRepo;

    @Autowired
    private VehicleRepo vehicleRepo;

    @Autowired
    private FuelBook_TypeRepo fuelBook_typeRepo;

    @Autowired
    private CompanyRepo companyRepo;

    @Transactional
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        // Validate required fields (without fuelBookId)
        if (transactionDTO.getUserId() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        if (transactionDTO.getVehicleId() == null) {
            throw new IllegalArgumentException("Vehicle ID must not be null");
        }
        if (transactionDTO.getFuelTypeId() == null) {
            throw new IllegalArgumentException("Fuel type ID must not be null");
        }
        if (transactionDTO.getCompanyId() == null) {
            throw new IllegalArgumentException("Company ID must not be null");
        }

        // Validate and retrieve entities (remove fuel book lookup)
        User user = usersRepo.findById(transactionDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + transactionDTO.getUserId()));

        Vehicle vehicle = vehicleRepo.findById(transactionDTO.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + transactionDTO.getVehicleId()));

        FuelBook_Type fuelBookType = fuelBook_typeRepo.findById(transactionDTO.getFuelTypeId())
                .orElseThrow(() -> new RuntimeException("Fuel type not found with id: " + transactionDTO.getFuelTypeId()));

        Company company = companyRepo.findById(transactionDTO.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + transactionDTO.getCompanyId()));

        // Create and save transaction (remove fuel book balance updates)
        Transaction transaction = new Transaction();
        transaction.setLiters(transactionDTO.getLiters());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setStatus(Transaction.TransactionStatus.PENDING);
        transaction.setUser(user);
        transaction.setVehicle(vehicle);
        transaction.setFuelBook_type(fuelBookType);
        transaction.setCompany(company);

        Transaction savedTransaction = transactionRepo.save(transaction);
        return mapToDTO(savedTransaction);
    }


    private TransactionDTO mapToDTO(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(transaction.getId());
        transactionDTO.setLiters(transaction.getLiters());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setStatus(transaction.getStatus());
        transactionDTO.setUserId(transaction.getUser().getId());
        transactionDTO.setVehicleId(transaction.getVehicle().getId());
        transactionDTO.setFuelTypeId(transaction.getFuelBook_type().getId());
        transactionDTO.setCompanyId(transaction.getCompany().getId());
        return transactionDTO;
    }
}