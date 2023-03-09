package com.karmi.project.Interface;

import com.karmi.project.entitie.EmailDetail;
import com.karmi.project.entitie.Holiday;
import com.karmi.project.entitie.MedicalStaff;

import javax.mail.MessagingException;

public interface IEmailServive {
    // To send a simple email
   public String sendSimpleMail(EmailDetail details);

    // Method
    // To send an email with attachment
     public String sendMailWithAttachment(Holiday  holiday, MedicalStaff medicalStaff ,String x) throws MessagingException;

}
