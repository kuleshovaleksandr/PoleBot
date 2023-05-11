package com.example.polebot.parser;

import com.example.polebot.model.Currency;
import com.example.polebot.sender.Sender;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CommandParser implements Parser {

    @Autowired private Sender sender;

    @Override
    public void parse(long chatId, String message) {
        sender.setChatId(chatId);
        switch(message) {
            case "/start":
//                registerUser(update.getMessage());
//                addKeyBoardMarkup();
                break;
            case "/currency":
                showCurrencyMenu();
                break;
            case "/gif":
//                sendGif(chatId, "cat");
                break;
            case "/sticker":
//                sendSticker();
                break;
            case "/animation":
                break;
        }
    }

    @SneakyThrows
    private void showCurrencyMenu() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        for(Currency currency: Currency.values()) {
            buttons.add(
                    Arrays.asList(
                            InlineKeyboardButton.builder()
                                    .text(currency.toString())
                                    .callbackData("ORIGINAL:" + currency)
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text(currency.toString())
                                    .callbackData("TARGET:" + currency)
                                    .build()
                    )
            );
        }
        String text = "Choose original and target currency\n" +
                "-----------------------------------------------------\n" +
                "       ORIGINAL                           TARGET";
        sender.sendInlineMessage(text, InlineKeyboardMarkup.builder().keyboard(buttons).build());
    }
}
