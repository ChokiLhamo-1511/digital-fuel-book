package com.Digital.Fuel.Book.Digital.Fuel.Book.controller;

import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.TransactionDTO;
import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.Transaction;
import com.Digital.Fuel.Book.Digital.Fuel.Book.service.impl.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        TransactionDTO createdTransaction = transactionService.createTransaction(transactionDTO);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }






}