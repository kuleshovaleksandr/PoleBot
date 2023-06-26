package com.example.polebot.parser;

import com.example.polebot.PoleBot;
import com.example.polebot.model.Command;
import com.example.polebot.model.Currency;
import com.example.polebot.model.NeuralLoveArtStyle;
import com.example.polebot.sender.MessageSender;
import com.example.polebot.service.impl.ChatGptService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Message;
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

    @Getter
    private String imageRequest;
    private final String INFO_MESSAGE = "*/currency* - this command shows you a menu where " +
            "you can choose _original_ and _target_ currencies. " +
            "Next message has to contain a number you want to change. " +
            "Currency rates are taken from *National Bank of the Republic of Belarus*.\n\n" +
            "*/gpt {your request}* - send your request to *ChatGpt*.\n\n" +
            "*/image {your request}* - send your request to *AI Image Generator*. " +
            "Then you should choose style of your image from a menu.\n\n" +
            "You can transcribe you voice message to text " +
            "and send it to *ChatGpt* as well.";

    @PostConstruct
    private void initCommands() {
        try {
            bot.execute(new SetMyCommands(Command.getBotCommands(), new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @Override
    public void parse(Message message) {
        sender.setChatId(message.getChatId());
        String text = message.getText();
        String infoCommand = Command.INFO.getName();
        String currencyCommand = Command.CURRENCY.getName();
        System.out.println(message.getChat().getType());
        if(message.getChat().getType().equals("group") ||
                message.getChat().getType().equals("supergroup")) {
            infoCommand += "@" + bot.getMe().getUserName();
            currencyCommand += "@" + bot.getMe().getUserName();
        }

        if(text.equals(infoCommand)) {
            sender.sendMarkdownMessage(INFO_MESSAGE);
        } else if(text.equals(currencyCommand)) {
            showCurrencyMenu();
        } else if(text.startsWith("/gpt")) {
            sendGptRequest(text);
        } else if(text.startsWith("/image")) {
            //TODO check if request exists
            imageRequest = text.substring(7);
            showImageStyleMenu();
        } else if(text.equals("/clean")) {
            chatGptService.initChat();
        }
    }

    private void sendGptRequest(String message) {
        //TODO check if request exists
        String request = message.substring(5);
        String response = chatGptService.getChatGptResponse(request);
        sender.sendMarkdownMessage("*ChatGPT*: " + "_" + response + "_");
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

//    private void sendJokeWithRequest(String request) {
//        String joke = humorService.searchJoke(request);
//        sender.sendMessage(joke);
//    }
//
//    private void sendMemeWithRequest(String request) {
//        String memeUrl = humorService.searchMeme(request);
//        sender.sendPhoto(memeUrl);
//    }
//
//    private String parseHumorRequest(String message) {
//        System.out.println("message = " + message);
//        String request = message.substring(6);
//        request = request.trim();
//        Pattern pattern = Pattern.compile("[[,\\s]\\s]");
//        Matcher matcher = pattern.matcher(request);
//        request = matcher.replaceAll(",");
//        return request;
//    }
}
