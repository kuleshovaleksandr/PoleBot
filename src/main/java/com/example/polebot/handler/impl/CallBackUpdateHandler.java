package com.example.polebot.handler.impl;

import com.example.polebot.handler.UpdateHandler;
import com.example.polebot.model.Currency;
import com.example.polebot.model.NeuralLoveArtStyle;
import com.example.polebot.parser.CommandParser;
import com.example.polebot.sender.MessageSender;
import com.example.polebot.service.impl.ChatGptService;
import com.example.polebot.service.impl.NeuralLoveService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@Component
public class CallBackUpdateHandler implements UpdateHandler {

    @Autowired private MessageSender sender;
    @Autowired private ChatGptService chatGptService;
    @Autowired private NeuralLoveService neuralLoveService;
    @Autowired private VoiceUpdateHandler voiceUpdateHandler;
    @Autowired private CommandParser commandParser;

    private HashMap<String, Currency> currencyChoice = new HashMap<>();
    private String callbackData;

    @PostConstruct
    public void init() {
        currencyChoice.put("ORIGINAL", null);
        currencyChoice.put("TARGET", null);
    }

    @Override
    public void handleUpdate(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String callbackData = callbackQuery.getData();
        if(callbackData.contains(":")) {
            saveCurrencyChoice(callbackData);
        } else if(callbackData.equals("Transcribe")) {
            sendVoiceTranscription();
        } else if(callbackData.equals("Send to ChatGPT")) {
            sendRequestToChatGpt();
        } else {
            for(NeuralLoveArtStyle style: NeuralLoveArtStyle.values()) {
                if(callbackData.equals(style.toString())) {
                    sendGeneratedImage(commandParser.getImageRequest(), style.getStyle());
                }
            }
        }
    }

    private void sendGeneratedImage(String request, String style) {
        sender.sendMarkdownMessage("_Wait while your image is being generated..._");
        List<String> imageUrlList = neuralLoveService.generateImage(request, style);
        for(String imageUrl: imageUrlList) {
            sender.sendPhoto(imageUrl);
        }
    }

    private void sendVoiceTranscription() {
        Chat chat = voiceUpdateHandler.getChat();
        String transcription = chatGptService.getVoiceTranscription();
        sender.sendMarkdownMessage("*" + chat.getUserName() + "*: " + transcription);
    }

    private void sendRequestToChatGpt() {
        String transcription = chatGptService.getVoiceTranscription();
        sender.sendMarkdownMessage("_Please wait for response from the server..._\n" +
                "*Your request*: " + transcription);
        String response = chatGptService.getChatGptResponse(transcription);
        sender.sendMarkdownMessage("*ChatGPT*: _" + response + "_");
    }

    private void saveCurrencyChoice(String callbackData) {
        String[] param = callbackData.split(":");
        String action = param[0];
        Currency currency = Currency.valueOf(param[1]);
        if(action.equals("ORIGINAL") || action.equals("TARGET")) {
            currencyChoice.put(action, currency);
        }
    }
}
