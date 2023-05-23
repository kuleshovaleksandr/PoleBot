package com.example.polebot.parser;

import com.example.polebot.PoleBot;
import com.example.polebot.model.Command;
import com.example.polebot.model.Currency;
import com.example.polebot.model.NeuralLoveArtStyle;
import com.example.polebot.sender.MessageSender;
import com.example.polebot.service.impl.ChatGptService;
import com.example.polebot.service.impl.NeuralLoveService;
import com.example.polebot.service.impl.YandexForecastService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Component
public class CommandParser implements Parser {

    @Autowired private MessageSender sender;
    @Autowired private PoleBot bot;
    @Autowired private ChatGptService chatGptService;
    @Autowired private NeuralLoveService neuralLoveService;
    @Autowired private YandexForecastService yandexForecastService;

    private String imageRequest;

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
//            yandexForecastService.getCurrentForecast();
        } else if(message.equals(Command.CURRENCY.getName())) {
            showCurrencyMenu();
        } else if(message.startsWith("/gpt")) {
            sendGptRequest(message);
        } else if(message.startsWith("/image")) {
            imageRequest = message.substring(7);
            showImageStyleMenu();
        }
    }

    private void sendInfo() {
        sender.sendMessage("info");
    }



    private void sendGptRequest(String message) {
        String request = message.substring(5);
        String response = chatGptService.getChatGptResponse(request);
        sender.sendMessage(response);
    }

    @SneakyThrows
    private void showImageStyleMenu() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        for(int i = 0; i < NeuralLoveArtStyle.values().length;) {
            List<InlineKeyboardButton> row = new ArrayList();
            for(int j = 0; j < 3; j++) {
                if(i < NeuralLoveArtStyle.values().length) {
                    row.add(InlineKeyboardButton.builder()
                            .text(NeuralLoveArtStyle.values()[i].getStyle())
                            .callbackData(NeuralLoveArtStyle.values()[i].toString())
                            .build());
                }
                i++;
            }
            buttons.add(row);
        }
        String text = "Please choose style for your image";
        sender.sendInlineMessage(text, InlineKeyboardMarkup.builder().keyboard(buttons).build());
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
