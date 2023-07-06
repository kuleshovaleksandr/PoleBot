package com.example.polebot.handler.impl;

import com.example.polebot.exception.ConnectionTimeOutException;
import com.example.polebot.handler.UpdateHandler;
import com.example.polebot.model.Currency;
import com.example.polebot.model.NeuralLoveArtStyle;
import com.example.polebot.parser.CommandParser;
import com.example.polebot.sender.MessageSender;
import com.example.polebot.service.impl.ChatGptService;
import com.example.polebot.service.impl.NeuralLoveService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class CallBackUpdateHandler implements UpdateHandler {

    @Autowired private MessageSender sender;
    @Autowired private ChatGptService chatGptService;
    @Autowired private NeuralLoveService neuralLoveService;
    @Autowired private VoiceUpdateHandler voiceUpdateHandler;
    @Autowired private CommandParser commandParser;

    @Getter
    private HashMap<String, Currency> currencyChoice = new HashMap<>();

    @PostConstruct
    public void setDefaultValues() {
        currencyChoice.put("ORIGINAL", null);
        currencyChoice.put("TARGET", null);
    }

    @Override
    public void handleUpdate(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String callbackData = callbackQuery.getData();
        //TODO block button if user clicked it until request appears
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
        try {
            List<String> imageUrlList = neuralLoveService.generateImage(request, style);
            for(String imageUrl: imageUrlList) {
                sender.sendPhoto(imageUrl);
            }
            log.info("Image generated with request " + request);
        } catch(ConnectionTimeOutException e) {
            log.error("Connection time out error while generating image");
            sender.sendMarkdownMessage("_" + e.getMessage() + "_");
        }
    }

    private void sendVoiceTranscription() {
        User user = voiceUpdateHandler.getUser();
        String transcription = chatGptService.getVoiceTranscription();
        sender.sendMarkdownMessage("*" + user.getUserName() + "*: " + transcription);
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
