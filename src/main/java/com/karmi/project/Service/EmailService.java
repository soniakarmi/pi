package com.karmi.project.Service;

import com.karmi.project.Interface.IEmailServive;
import com.karmi.project.entitie.EmailDetail;
import com.karmi.project.entitie.Holiday;
import com.karmi.project.entitie.MedicalStaff;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Slf4j

@Service
public class EmailService  implements IEmailServive {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }



    @Override
    public String sendSimpleMail(EmailDetail details) {
        return null;
    }

    @Override
    public String sendMailWithAttachment(Holiday holiday, MedicalStaff medicalStaff,String x) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(medicalStaff.getEmail());
        helper.setSubject("Appointment Invitation");

        Context  context = new Context();
        context.setVariable("holiday", holiday);
        context.setVariable("MedicalStaff",medicalStaff );
        context.setVariable("x", x);
        System.out.println(holiday.getType());
        String html = templateEngine.process("mail_template", context);

        helper.setText(html, true);

          javaMailSender.send(message);
        return html;
    }

    // Method 2
    // To send an email with attachment

}





