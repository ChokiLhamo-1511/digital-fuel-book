package com.Digital.Fuel.Book.Digital.Fuel.Book.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendThresholdNotification(String toEmail, String vehicleId,
                                          double totalPending, double threshold) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("🚨 Fuel Threshold Reached for Vehicle " + vehicleId);

            String emailContent = buildEmailContent(vehicleId, totalPending, threshold);
            helper.setText(emailContent, true);

            javaMailSender.send(message);
            logger.info("Email sent successfully to: {}", toEmail);
        } catch (MailException e) {
            logger.error("Failed to send email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send email notification", e);
        } catch (MessagingException e) {
            logger.error("Messaging exception while sending email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Email composition error", e);
        }
    }

    private String buildEmailContent(String vehicleId, double totalPending, double threshold) {
        return String.format(
                "<html>" +
                        "<head>" +
                        "  <style>" +
                        "    body { font-family: Arial, sans-serif; line-height: 1.6; }" +
                        "    .header { color: #d9534f; font-size: 1.5em; margin-bottom: 20px; }" +
                        "    table { border-collapse: collapse; width: 100%%; margin: 15px 0; }" +
                        "    th, td { padding: 8px; text-align: left; border: 1px solid #ddd; }" +
                        "    th { background-color: #f5f5f5; }" +
                        "    .footer { margin-top: 20px; font-size: 0.9em; color: #777; }" +
                        "  </style>" +
                        "</head>" +
                        "<body>" +
                        "  <div class='header'>Fuel Threshold Alert</div>" +
                        "  <p>The fuel threshold has been reached for vehicle <strong>%s</strong>.</p>" +
                        "  <table>" +
                        "    <tr><th>Total Pending Amount</th><td>%.2f</td></tr>" +
                        "    <tr><th>Threshold Amount</th><td>%.2f</td></tr>" +
                        "  </table>" +
                        "  <p>Please review and approve the pending transactions in the system.</p>" +
                        "  <div class='footer'>" +
                        "    This is an automated notification. Please do not reply to this email." +
                        "  </div>" +
                        "</body>" +
                        "</html>",
                vehicleId, totalPending, threshold
        );
    }
}