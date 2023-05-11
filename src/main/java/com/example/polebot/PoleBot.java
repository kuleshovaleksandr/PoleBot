package com.example.polebot;

import com.example.polebot.config.BotConfig;
import com.example.polebot.entity.User;
import com.example.polebot.handler.UpdateHandlerFactory;
import com.example.polebot.handler.UpdateHandlerStage;
import com.example.polebot.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class PoleBot extends TelegramLongPollingBot {

    @Autowired private BotConfig botConfig;

    @Autowired private UpdateHandlerFactory updateHandlerFactory;
    @Autowired private UserRepository userRepository;

    private List<BotCommand> listOfCommands;

    @PostConstruct
    public void initCommands() {
        listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "give a welcome message"));
        listOfCommands.add(new BotCommand("/mydata", "get your data stored"));
        listOfCommands.add(new BotCommand("/currency", "change currency"));

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            updateHandlerFactory.getUpdateHandler(UpdateHandlerStage.TEXT)
                    .handleUpdate(update);
        } else if(update.hasCallbackQuery()) {
            updateHandlerFactory.getUpdateHandler(UpdateHandlerStage.CALLBACK)
                    .handleUpdate(update);
        } else if(update.getMessage().hasAnimation()) {
            updateHandlerFactory.getUpdateHandler(UpdateHandlerStage.ANIMATION)
                    .handleUpdate(update);
        } else if(update.getMessage().hasSticker()) {
            updateHandlerFactory.getUpdateHandler(UpdateHandlerStage.STICKER)
                    .handleUpdate(update);
        }
    }

    private void registerUser(Message message) {
        if(userRepository.findById(message.getChatId()).isEmpty()) {
            long id = message.getChatId();
            Chat chat = message.getChat();

            User user = new User();
            user.setId(id);
            user.setUserName(chat.getUserName());
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
        }
    }

    @SneakyThrows
    private void addKeyBoardMarkup() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("weather");
        row.add("get a joke");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("register");
        row.add("check my data");
        row.add("delete my data");
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
//        message.setReplyMarkup(keyboardMarkup);
//        execute(message);
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