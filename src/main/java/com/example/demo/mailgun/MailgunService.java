package com.example.demo.mailgun;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.model.message.Message;
import com.mailgun.model.message.MessageResponse;

@Service
public class MailgunService {

    @Autowired
    private MailgunMessagesApi mailgunMessagesApi;


    @Value("${mailgun.domain}")
    private String domain;

    public void sendSimpleEmail(String from,String to, String subject, String text) {
        Message message = Message.builder()
                .from(from)
                .to(to)
                .subject(subject)
                .text(text)
                .build();

        MessageResponse response = mailgunMessagesApi.sendMessage(domain, message);
        System.out.println("Email sent response: " + response.getId());
    }
}
