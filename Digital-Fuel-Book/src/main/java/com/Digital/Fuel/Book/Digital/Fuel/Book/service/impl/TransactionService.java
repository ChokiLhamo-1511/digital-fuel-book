package com.Digital.Fuel.Book.Digital.Fuel.Book.service.impl;

import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.TransactionDTO;
import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.*;
import com.Digital.Fuel.Book.Digital.Fuel.Book.exception.ThresholdNotMetException;
import com.Digital.Fuel.Book.Digital.Fuel.Book.repo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

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

    @Autowired
    private EmailService emailService;

    @Value("${notification.email}")
    private String notificationEmail;

    @Transactional
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        validateTransactionDTO(transactionDTO);

        // Retrieve entities
        User user = usersRepo.findById(transactionDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Vehicle vehicle = vehicleRepo.findById(transactionDTO.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        FuelBook_Type fuelType = fuelBook_typeRepo.findById(transactionDTO.getFuelTypeId())
                .orElseThrow(() -> new RuntimeException("Fuel type not found"));
        Company company = companyRepo.findById(transactionDTO.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));
        FuelBook fuelBook = fuelBookRepo.findByVehicleId(transactionDTO.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Fuel book not found"));

        // Create and save transaction
        Transaction transaction = createNewTransaction(transactionDTO, user, vehicle, fuelType, company);
        Transaction savedTransaction = transactionRepo.save(transaction);

        // Calculate pending total
        double totalPending = calculatePendingTotal(transactionDTO.getVehicleId());

        // Prepare response
        TransactionDTO response = mapToDTO(savedTransaction);
        response.setMessage("Transaction added successfully");

        // Check threshold and send email if reached
        if (totalPending >= fuelBook.getThresholdAmount()) {
            logger.info("Threshold reached for vehicle {} ({} >= {})",
                    vehicle.getId(), totalPending, fuelBook.getThresholdAmount());

            try {
                emailService.sendThresholdNotification(
                        notificationEmail,
                        vehicle.getId().toString(),
                        totalPending,
                        fuelBook.getThresholdAmount()
                );
                response.setMessage("Threshold reached! Notification sent to administrator");
            } catch (Exception e) {
                logger.error("Failed to send notification email", e);
                response.setMessage("Threshold reached but failed to send notification");
            }
        }

        return response;
    }

    private double calculatePendingTotal(Long vehicleId) {
        List<Transaction> pendingTransactions = transactionRepo.findByVehicleIdAndStatus(
                vehicleId,
                Transaction.TransactionStatus.PENDING
        );
        return pendingTransactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }


    private void validateTransactionDTO(TransactionDTO transactionDTO) {
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
        if (transactionDTO.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (transactionDTO.getLiters() <= 0) {
            throw new IllegalArgumentException("Liters must be greater than zero");
        }
    }

    private void checkThresholdRequirement(TransactionDTO transactionDTO) {
        FuelBook fuelBook = fuelBookRepo.findByVehicleId(transactionDTO.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Fuel book not found for vehicle id: " + transactionDTO.getVehicleId()));

        List<Transaction> pendingTransactions = transactionRepo.findByVehicleIdAndStatus(
                transactionDTO.getVehicleId(),
                Transaction.TransactionStatus.PENDING
        );

        double sumPendingAmounts = pendingTransactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalAmountWithNewTransaction = sumPendingAmounts + transactionDTO.getAmount();

        if (totalAmountWithNewTransaction < fuelBook.getThresholdAmount()) {
            throw new ThresholdNotMetException(
                    String.format("Total pending amount (%.2f) plus new transaction (%.2f) = %.2f is less than threshold (%.2f)",
                            sumPendingAmounts,
                            transactionDTO.getAmount(),
                            totalAmountWithNewTransaction,
                            fuelBook.getThresholdAmount()
                    )
            );
        }
    }

    private Transaction createNewTransaction(TransactionDTO transactionDTO, User user, Vehicle vehicle,
                                             FuelBook_Type fuelBookType, Company company) {
        Transaction transaction = new Transaction();
        transaction.setLiters(transactionDTO.getLiters());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setStatus(Transaction.TransactionStatus.PENDING);
        transaction.setUser(user);
        transaction.setVehicle(vehicle);
        transaction.setFuelBook_type(fuelBookType);
        transaction.setCompany(company);
        return transaction;
    }

    public List<TransactionDTO> getAllTransactions() {
        List<Transaction> transactions = transactionRepo.findAll();
        return transactions.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
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
        FuelBook_Type fuelType = transaction.getFuelBook_type();
        transactionDTO.setFuelTypeId(fuelType.getId());
        transactionDTO.setFuel_type((fuelType.getFuel_type()));

        FuelBook fuelBook = fuelBookRepo.findByVehicleId(transaction.getVehicle().getId())
                .orElse(null);

        if (fuelBook != null) {
            transactionDTO.setThresholdAmount(fuelBook.getThresholdAmount());

            // Calculate current pending total for this vehicle
            double pendingTotal = calculatePendingTotal(transaction.getVehicle().getId());
            transactionDTO.setPendingTotal(pendingTotal);
        }

        return transactionDTO;


    }
}