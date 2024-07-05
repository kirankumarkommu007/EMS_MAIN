package com.example.demo.mailchrimp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private MailgunService mailgunService;

    @PostMapping("/sendEmail")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        String to = emailRequest.getTo();
        String subject = emailRequest.getSubject();
        String text = emailRequest.getText();

        mailgunService.sendSimpleEmail(to, subject, text);

        return "Email sent successfully!";
    }
}

