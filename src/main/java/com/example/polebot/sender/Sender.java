package com.example.polebot.sender;

import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface Sender {

    void sendMessage(String text);

    void sendAnimation(InputFile inputFile);

    void sendSticker(InputFile inputFile);

    void replyWithInlineMessageTo(int messageId, String text, InlineKeyboardMarkup inlineKeyboardMarkup);

    void editText(int messageId, String text);

    void sendInlineVoice(InputFile inputFile, InlineKeyboardMarkup inlineKeyboardMarkup);

    void sendInlineMessage(String text, InlineKeyboardMarkup inlineKeyboardMarkup);
}
