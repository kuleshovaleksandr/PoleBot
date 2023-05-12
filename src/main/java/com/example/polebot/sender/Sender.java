package com.example.polebot.sender;

import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface Sender {

    void sendMessage(String text);

    void sendAnimation(InputFile inputFile);

    void sendSticker(InputFile inputFile);

    void editText(int messageId, String text);

    void sendInlineMessage(String text, InlineKeyboardMarkup inlineKeyboardMarkup);
}
