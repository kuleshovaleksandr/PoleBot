package com.example.polebot.sender;

import com.example.polebot.PoleBot;
import com.example.polebot.entity.Animation;
import com.example.polebot.model.WeekDay;
import com.example.polebot.service.StickerService;
import com.example.polebot.service.impl.DBAnimationService;
import com.example.polebot.service.impl.GiphyAnimationService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Calendar;
import java.util.Date;

@Slf4j
@Setter
@Getter
@Component
public class MessageSender implements Sender {

    @Autowired private PoleBot bot;
    @Autowired private StickerService stickerService;
    @Autowired private DBAnimationService dbAnimationService;
    @Autowired private GiphyAnimationService giphyAnimationService;

    private long chatId;

    @Override
    public void sendMessage(String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        executeMessage(message);
    }

    @Override
    public void sendMarkdownMessage(String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode("Markdown")
                .build();
        executeMessage(message);
    }

    @Override
    public void sendInlineMessage(String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
        executeMessage(message);
    }

    @Override
    public void replyWithInlineMessageTo(int messageId, String text,
                                         InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyToMessageId(messageId)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
        executeMessage(message);
    }

    @Override
    public void sendInlineVoice(InputFile inputFile, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendVoice voice = SendVoice.builder()
                .chatId(chatId)
                .voice(inputFile)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
        try {
            bot.execute(voice);
            log.info("Media executed");
        } catch(TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void sendAnimation(InputFile inputFile) {
        SendAnimation sendAnimation = SendAnimation.builder()
                .chatId(chatId)
                .animation(inputFile)
                .build();
        try {
            bot.execute(sendAnimation);
            log.info("Media executed");
        } catch(TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void sendSticker(InputFile inputFile) {
        SendSticker sendSticker = SendSticker.builder()
                .chatId(chatId)
                .sticker(new InputFile(stickerService.getStickerByEmoji("\uD83E\uDE9F").getFileId()))
                .build();
        try {
            bot.execute(sendSticker);
            log.info("Media executed");
        } catch(TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void sendPhoto(String imageUrl) {
        SendPhoto sendPhoto = SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(imageUrl))
                .build();
        try {
            bot.execute(sendPhoto);
            log.info("Media executed");
        } catch(TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void editText(int messageId, String text) {
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .text(text)
                .messageId(messageId)
                .build();
        try {
            bot.execute(message);
        } catch(TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendGif(String tag) {
        String gifUrl = giphyAnimationService.getRandomAnimation(tag);
        sendAnimation(new InputFile(gifUrl));
    }

    private void executeMessage(BotApiMethod<Message> message) {
        try {
            bot.execute(message);
            log.info("Message executed");
        } catch(TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Scheduled(cron="${cron.scheduler.animation}")
    private void sendWeekDayAnimation() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        WeekDay[] daysOfWeek = WeekDay.values();
        Animation animation = dbAnimationService.getRandomWeekDayAnimation(daysOfWeek[currentDay-1]);
        sendAnimation(new InputFile(animation.getFileId()));
    }
}
