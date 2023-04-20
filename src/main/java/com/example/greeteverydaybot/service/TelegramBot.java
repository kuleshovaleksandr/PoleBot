package com.example.greeteverydaybot.service;

import com.example.greeteverydaybot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private BotConfig botConfig;

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String firstName = update.getMessage().getChat().getFirstName();
            long chatId = update.getMessage().getChatId();

            switch(messageText) {
                case "/start":
                    startCommandReceived(chatId, firstName);
                    break;
//                default: sendMessage(chatId, "Sorry, command was not recognized.");
            }

            findWordTesla(chatId, messageText);
        }
    }

    private void findWordTesla(long chatId, String messageText) {
        Pattern pattern = Pattern.compile("[Тт]есл.");
        Matcher matcher = pattern.matcher(messageText);

        while(matcher.find()) {
            sendMessage(chatId, "you are boring");
        }
    }

    private void startCommandReceived(long chatId, String firstName) {
        String answer = "Hello, " + firstName + ", nice to meet you!";
        log.info("Replied to user " + firstName);
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }

    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }
}
