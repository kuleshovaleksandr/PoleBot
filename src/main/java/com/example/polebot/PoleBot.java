package com.example.polebot;

import com.example.polebot.config.BotConfig;
import com.example.polebot.handler.UpdateHandlerFactory;
import com.example.polebot.handler.UpdateHandlerStage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Component
public class PoleBot extends TelegramLongPollingBot {

    @Autowired private BotConfig botConfig;
    @Autowired private UpdateHandlerFactory updateHandlerFactory;

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        } catch(TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Bot updated");
        if(update.hasMessage() && update.getMessage().hasText()) {
            updateHandlerFactory.getUpdateHandler(UpdateHandlerStage.TEXT)
                    .handleUpdate(update);
        } else if(update.hasCallbackQuery()) {
            updateHandlerFactory.getUpdateHandler(UpdateHandlerStage.CALLBACK)
                    .handleUpdate(update);
        } else if(update.hasMessage() && update.getMessage().hasAnimation()) {
            updateHandlerFactory.getUpdateHandler(UpdateHandlerStage.ANIMATION)
                    .handleUpdate(update);
        } else if(update.hasMessage() && update.getMessage().hasSticker()) {
            updateHandlerFactory.getUpdateHandler(UpdateHandlerStage.STICKER)
                    .handleUpdate(update);
        } else if(update.hasMessage() && update.getMessage().hasVoice()) {
            updateHandlerFactory.getUpdateHandler(UpdateHandlerStage.VOICE)
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