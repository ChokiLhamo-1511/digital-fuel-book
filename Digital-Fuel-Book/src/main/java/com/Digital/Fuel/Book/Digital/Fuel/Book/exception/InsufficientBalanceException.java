package com.Digital.Fuel.Book.Digital.Fuel.Book.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
