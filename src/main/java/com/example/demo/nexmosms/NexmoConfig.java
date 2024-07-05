package com.example.demo.nexmosms;


import com.vonage.client.VonageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NexmoConfig {

    @Value("${nexmo.api-key}")
    private String apiKey;

    @Value("${nexmo.api-secret}")
    private String apiSecret;

    @Bean
    public VonageClient vonageClient() {
        return VonageClient.builder()
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build();
    }
}
