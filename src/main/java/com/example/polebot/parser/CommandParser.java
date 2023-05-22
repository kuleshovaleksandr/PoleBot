package com.example.polebot.parser;

import com.example.polebot.PoleBot;
import com.example.polebot.model.Command;
import com.example.polebot.model.Currency;
import com.example.polebot.sender.MessageSender;
import com.example.polebot.service.ForecastService;
import com.example.polebot.service.impl.ChatGptService;
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

    @Autowired private MessageSender sender;
    @Autowired private PoleBot bot;
    @Autowired private ChatGptService chatGptService;
    @Autowired private ForecastService forecastService;

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
        if(message.equals(Command.INFO.getName())) {
            sendInfo();
        } else if(message.equals(Command.FORECAST.getName())) {
            forecastService.getCurrentForecast();
        } else if(message.equals(Command.CURRENCY.getName())) {
            showCurrencyMenu();
        } else if(message.startsWith("/gpt")) {
            sendGptRequest(message);
        }
    }

    private void sendInfo() {
        sender.sendMessage("info");
    }

    private void sendGptRequest(String message) {
        String chatRequest = message.substring(5);
        String chatResponse = chatGptService.getChatGptResponse(chatRequest);
        sender.sendMessage(chatResponse);
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
