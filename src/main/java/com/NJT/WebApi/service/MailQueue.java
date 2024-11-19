package com.NJT.WebApi.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class MailQueue {

    private JavaMailSender mailSender;

    @Autowired
    public MailQueue(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private List<SimpleMailMessage> queue;

    @PostConstruct
    public void init() {
        queue = new ArrayList<>();
    }

    public void addToQueue(SimpleMailMessage mail){
        queue.add(mail);
    }

    @Scheduled(fixedRate = 20000)
    public void sendMail() {
        synchronized (queue) {  // Synchronize on the queue to ensure thread safety
            Iterator<SimpleMailMessage> iterator = queue.iterator();
            while (iterator.hasNext()) {
                SimpleMailMessage mail = iterator.next();
                try {
                    mailSender.send(mail);
                    iterator.remove();
                } catch (MailException ex) {
                    System.out.println("Failed to send email!");
                }
            }
        }
    }

}
