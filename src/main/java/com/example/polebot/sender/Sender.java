package com.example.polebot.sender;

import com.example.polebot.PoleBot;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;


@Component
public class Sender {

    @Autowired private PoleBot bot;

    @SneakyThrows
    public void sendMessage(long chatId, String text) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void sendInlineMessage(long chatId, String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
        bot.execute(message);
    }

    @SneakyThrows
    public void sendAnimation(long chatId, String text) {

    }
}
