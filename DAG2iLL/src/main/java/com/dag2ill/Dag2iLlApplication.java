package com.dag2ill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class Dag2iLlApplication {

	public static void main(String[] args) {
		SpringApplication.run(Dag2iLlApplication.class, args);
	}

	 @Bean
	    public TelegramBotsApi telegramBotsApi(PdfTelegramBot bot) throws Exception {
	        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
	        api.registerBot(bot);
	        return api;
	    }
}
