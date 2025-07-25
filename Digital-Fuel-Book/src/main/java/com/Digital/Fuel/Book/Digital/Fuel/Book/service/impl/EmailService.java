package com.Digital.Fuel.Book.Digital.Fuel.Book.service.impl;

import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.Company;
import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.FuelBook;
import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }
    public  void sendEmail(String to,String subject,String body){
        try{
            MimeMessage message =javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body,false);
            javaMailSender.send(message); // Send the email
        }catch (MessagingException e){
            e.printStackTrace();
            e.getMessage();
            throw new RuntimeException(e);
        }
    }

}
