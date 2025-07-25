package com.Digital.Fuel.Book.Digital.Fuel.Book.repo;

import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction,Long> {
}
