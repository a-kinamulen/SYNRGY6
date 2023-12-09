package com.kinamulen.notificationservice.service;

import com.kinamulen.notificationservice.dto.NotificationOtpWebRequest;
import com.kinamulen.notificationservice.dto.NotificationWebRequest;
import com.kinamulen.notificationservice.dto.NotificationWebResponse;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Service
public class NotificationService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(NotificationWebRequest request) {
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(request.getReceiverEmail()));
            mimeMessage.setFrom(new InternetAddress("a.kinamulen@gmail.com")); //admin email
            mimeMessage.setSubject("Order Confirmation");

            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText("Your invoice attached below:");

            //now write the PDF content to the output stream
            byte[] bytes = Base64.decode(request.getPdfByte());

            //construct the pdf body part
            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
            pdfBodyPart.setFileName("Invoice.pdf");

            //construct the mime multi part
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(textBodyPart);
            mimeMultipart.addBodyPart(pdfBodyPart);
            mimeMessage.setContent(mimeMultipart);
        };
        try {
            mailSender.send(preparator);
        } catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
        }
    }

    public void sendOtpViaEmail(NotificationOtpWebRequest request) {
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(request.getReceiverEmail()));
            mimeMessage.setFrom(new InternetAddress("a.kinamulen@gmail.com")); //admin email
            mimeMessage.setSubject("Your registration OTP for BinarFood!");

            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText("This is your OTP: " + request.getOtp());

            //construct the mime multi part
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(textBodyPart);
            mimeMessage.setContent(mimeMultipart);
        };
        try {
            mailSender.send(preparator);
        } catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
        }
    }
}