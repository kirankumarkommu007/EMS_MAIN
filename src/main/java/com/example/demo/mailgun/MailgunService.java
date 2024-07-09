package com.example.demo.mailgun;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.model.message.Message;


@Service
public class MailgunService {
	

	private final MailgunMessagesApi mailgunMessagesApi;

	public MailgunService(MailgunMessagesApi mailgunMessagesApi) {
		this.mailgunMessagesApi = mailgunMessagesApi;
	}

	@Value("${mailgun.domain}")
	private String domain;

	public void sendSimpleEmail(String from, String to, String subject, String text) {
		Message message = Message.builder().from(from).to(to).subject(subject).text(text).build();

		mailgunMessagesApi.sendMessage(domain, message);
	}
}
