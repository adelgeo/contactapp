package com.adel.contactform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailComponent {

    @Autowired
    MailSender mailSender;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    TemplateEngine templateEngine;

    public boolean sendSimpleMail(Contact contact) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(contact.getEmail());
        mailMessage.setTo("adel.boudjema@gmail.com");
        mailMessage.setSubject(contact.getSubject());
        mailMessage.setText(contact.getMessage());


        try {
            mailSender.send(mailMessage);
            return true;
        } catch (MailException e) {
            return false;
        }

    }


    public boolean sendHtmlMail(Contact contact) {

        Context context = new Context();
        context.setVariable("contact", contact);
        final String content = templateEngine.process("email/contact", context);


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage);
        try {
            mailMessage.setFrom(contact.getEmail());
            mailMessage.setTo("adel.boudjema@gmail.com");
            mailMessage.setSubject(contact.getSubject());
            mailMessage.setText(content, true); //content replaced contact.getMessage()= Replaced simple text mail

            javaMailSender.send(mimeMessage);
            return true;
        } catch (MessagingException | MailException e) {
            return false;
        }

    }

}
