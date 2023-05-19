package com.example.polebot.handler.impl;

import com.example.polebot.handler.UpdateHandler;
import com.example.polebot.model.Currency;
import com.example.polebot.sender.MessageSender;
import com.example.polebot.service.impl.ChatGptService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;

@Getter
@Setter
@Component
public class CallBackUpdateHandler implements UpdateHandler {

    @Autowired private MessageSender sender;
    @Autowired private ChatGptService chatGptService;
    @Autowired private VoiceUpdateHandler voiceUpdateHandler;

    private HashMap<String, Currency> currencyChoice = new HashMap<>();

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
            Chat chat = voiceUpdateHandler.getChat();
            String transcription = chatGptService.getVoiceTranscription();
            sender.sendMessage(chat.getUserName() + " said: " + transcription);
        } else if(callbackData.equals("Send to ChatGPT")) {
            String transcription = chatGptService.getVoiceTranscription();
            sender.sendMessage("Please wait for request from the server...\n" +
                    "Your request: " + transcription);
            String response = chatGptService.getChatGptResponse(transcription);
            sender.sendMessage("ChatGPT: " + response);
        }
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
