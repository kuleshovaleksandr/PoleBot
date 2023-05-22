package com.example.polebot.sender;

import com.example.polebot.PoleBot;
import com.example.polebot.model.WeekDay;
import com.example.polebot.service.StickerService;
import com.example.polebot.service.impl.DBAnimationService;
import com.example.polebot.service.impl.GiphyAnimationService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Calendar;
import java.util.Date;

@Setter
@Getter
@Component
public class MessageSender implements Sender {

    @Autowired private PoleBot bot;
    @Autowired private StickerService stickerService;
    @Autowired private DBAnimationService dbAnimationService;
    @Autowired private GiphyAnimationService giphyAnimationService;

    @Value("${bot.pole.owner}")
    private long chatId;

    @SneakyThrows
    @Override
    public void sendMessage(String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        bot.execute(message);
    }

    @SneakyThrows
    @Override
    public void sendMarkdownMessage(String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode("Markdown")
                .build();
        bot.execute(message);
    }

    @SneakyThrows
    @Override
    public void sendInlineMessage(String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
        bot.execute(message);
    }

    @SneakyThrows
    @Override
    public void sendInlineVoice(InputFile inputFile, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendVoice voice = SendVoice.builder()
                .chatId(chatId)
                .voice(inputFile)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
        bot.execute(voice);
    }

    @SneakyThrows
    @Override
    public void replyWithInlineMessageTo(int messageId, String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyToMessageId(messageId)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
        bot.execute(message);
    }

    @SneakyThrows
    @Override
    public void sendAnimation(InputFile inputFile) {
        SendAnimation sendAnimation = SendAnimation.builder()
                .chatId(chatId)
                .animation(inputFile)
                .build();
        bot.execute(sendAnimation);
    }

    @SneakyThrows
    @Override
    public void sendSticker(InputFile inputFile) {
        SendSticker sendSticker = SendSticker.builder()
                .chatId(chatId)
                .sticker(new InputFile(stickerService.getStickerByEmoji("\uD83E\uDE9F").getFileId()))
                .build();
        bot.execute(sendSticker);
    }

    @SneakyThrows
    @Override
    public void editText(int messageId, String text) {
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .text(text)
                .messageId(messageId)
                .build();
        bot.execute(message);
    }

    @SneakyThrows
    private void sendGif(String tag) {
        String gifUrl = giphyAnimationService.getRandomAnimation(tag);
        sendAnimation(new InputFile(gifUrl));
    }

    @SneakyThrows
    @Scheduled(cron="${cron.scheduler}")
    private void sendWeekDayAnimation() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        WeekDay[] daysOfWeek = WeekDay.values();
        String animationId = dbAnimationService.getRandomWeekDayAnimation(daysOfWeek[currentDay-1]);
        sendAnimation(new InputFile(animationId));
    }
}
