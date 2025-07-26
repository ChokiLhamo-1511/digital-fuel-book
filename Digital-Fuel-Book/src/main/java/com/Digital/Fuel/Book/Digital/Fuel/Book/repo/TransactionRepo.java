package com.Digital.Fuel.Book.Digital.Fuel.Book.repo;

import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction,Long> {
    List<Transaction> findByVehicleIdAndStatus(Long vehicleId, TransactionStatus status);

    List<Transaction> findByVehicleIdAndStatus(Long vehicleId, Transaction.TransactionStatus transactionStatus);
}
