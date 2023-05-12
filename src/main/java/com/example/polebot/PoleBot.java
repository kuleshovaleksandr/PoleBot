package com.example.polebot;

import com.example.polebot.config.BotConfig;
import com.example.polebot.entity.User;
import com.example.polebot.handler.UpdateHandlerFactory;
import com.example.polebot.handler.UpdateHandlerStage;
import com.example.polebot.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class PoleBot extends TelegramLongPollingBot {

    @Autowired private BotConfig botConfig;

    @Autowired private UpdateHandlerFactory updateHandlerFactory;
    @Autowired private UserRepository userRepository;

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        } catch(TelegramApiException e) {
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



    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }
}