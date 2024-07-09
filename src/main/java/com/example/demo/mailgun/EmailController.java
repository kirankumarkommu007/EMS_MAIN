package com.example.demo.mailgun;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

	
    private final MailgunService mailgunService;
    
    public EmailController(MailgunService mailgunService) {
    	this.mailgunService=mailgunService;
    }

    @PostMapping("/sendEmail")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
    	String from =emailRequest.getFrom();
        String to = emailRequest.getTo();
        String subject = emailRequest.getSubject();
        String text = emailRequest.getText();

        mailgunService.sendSimpleEmail(from,to, subject, text);

        return "Email sent successfully!";
    }
}

