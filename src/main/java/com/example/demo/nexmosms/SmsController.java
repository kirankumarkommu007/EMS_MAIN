package com.example.demo.nexmosms;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsController {

    
    private final NexmoService nexmoService;
    public SmsController(NexmoService nexmoService) {
    	this.nexmoService =nexmoService;
    }

    @GetMapping("/send-sms")
    public String sendSms(@RequestParam String to, @RequestParam String message) {
        return nexmoService.sendSms(to, message);
    }
}
