package com.example.greeteverydaybot.service;

import com.example.greeteverydaybot.config.BotConfig;
import com.example.greeteverydaybot.entity.User;
import com.example.greeteverydaybot.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private BotConfig botConfig;

    @Autowired
    private UserRepository userRepository;

    private List<BotCommand> listOfCommands;
    private final static String HELP_TEXT = "This bot is created to help channel Pole.\n" +
            "You can execute commands from the main menu on the left\n" +
            "Type /start to see welcome message\n" +
            "Type /mydata to see your data";

    @PostConstruct
    public void initCommands() {
        listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "give a welcome message"));
        listOfCommands.add(new BotCommand("/mydata", "get your data stored"));
        listOfCommands.add(new BotCommand("/deletedata", "delete my data"));
        listOfCommands.add(new BotCommand("/help", "how to use this bot"));
        listOfCommands.add(new BotCommand("/settings", "set your preferences"));

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String firstName = update.getMessage().getChat().getFirstName();
            long chatId = update.getMessage().getChatId();

            switch(messageText) {
                case "/start":
                    registerUser(update.getMessage());
                    startCommandReceived(chatId, firstName);
                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;
                default: if(messageText.startsWith("/"))
                    sendMessage(chatId, "Sorry, command was not recognized.");
            }

            findWordTesla(chatId, messageText);
        }
    }

    private void registerUser(Message message) {
        if(userRepository.findById(message.getChatId()).isEmpty()) {
            var id = message.getChatId();
            var chat = message.getChat();

            User user = new User();
            user.setId(id);
            user.setUserName(chat.getUserName());
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
            log.info("User created: " + user);
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
