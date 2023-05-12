package com.example.polebot.parser;

import com.example.polebot.PoleBot;
import com.example.polebot.model.Command;
import com.example.polebot.model.Currency;
import com.example.polebot.sender.Sender;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CommandParser implements Parser {

    @Autowired private Sender sender;
    @Autowired private PoleBot bot;

    @PostConstruct
    public void initCommands() {
        try {
            bot.execute(new SetMyCommands(Command.getBotCommands(), new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parse(long chatId, String message) {
        sender.setChatId(chatId);
        if(message.equals(Command.JOKE.getName())) {
            sender.sendMessage("joke");
        } else if(message.equals(Command.FORECAST.getName())) {
            sender.sendMessage("forecast");
        } else if(message.equals(Command.CURRENCY.getName())) {
            showCurrencyMenu();
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
