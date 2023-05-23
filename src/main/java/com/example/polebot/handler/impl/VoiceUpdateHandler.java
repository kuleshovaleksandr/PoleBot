package com.example.polebot.handler.impl;

import com.example.polebot.converter.OggConverter;
import com.example.polebot.handler.UpdateHandler;
import com.example.polebot.sender.MessageSender;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class VoiceUpdateHandler implements UpdateHandler {

    @Autowired private MessageSender sender;
    @Autowired private OggConverter oggConverter;

    @Getter
    private Chat chat;
    private final String TRANSCRIBE = "Transcribe";
    private final String SEND_TO_CHAT_GPT = "Send to ChatGPT";

    @Override
    public void handleUpdate(Update update) {
        chat = update.getMessage().getChat();
        oggConverter.toMp3(update.getMessage().getVoice().getFileId());
        addVoiceButtons(update.getMessage().getMessageId());
        update.getMessage().getChatId();
    }

    private void addVoiceButtons(int messageId) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(
                Arrays.asList(
                        InlineKeyboardButton.builder()
                                .text(TRANSCRIBE)
                                .callbackData(TRANSCRIBE)
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(SEND_TO_CHAT_GPT)
                                .callbackData(SEND_TO_CHAT_GPT)
                                .build()
                )
        );
        String text = "What should I do with this voice message?";
        sender.replyWithInlineMessageTo(messageId, text, InlineKeyboardMarkup.builder().keyboard(buttons).build());
    }
}
