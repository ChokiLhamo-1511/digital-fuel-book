package com.Digital.Fuel.Book.Digital.Fuel.Book.service;

public interface EmailService {
    void sendThresholdAlertEmail(String to, String subject, String message);
}
