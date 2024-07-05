package com.example.demo.nexmosms;


import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NexmoService {

    @Autowired
    private VonageClient vonageClient;

    public String sendSms(String to, String text) {
        TextMessage message = new TextMessage("Nexmo", to, text);
        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(message);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            return "SMS sent successfully!";
        } else {
            return "Error: " + response.getMessages().get(0).getErrorText();
        }
    }
}
