package com.example.demo.mailgun;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;

@Configuration
public class MailgunConfig {

    @Value("${mailgun.api.key}")
    private String apiKey;

    @Value("${mailgun.domain}")
    private String domain;

    @Value("${mailgun.api.base.url}")
    private String apiUrl;

    @Bean
    public MailgunMessagesApi mailgunMessagesApi() {
        return MailgunClient.config(apiKey)
                .createApi(MailgunMessagesApi.class);
    }
}
