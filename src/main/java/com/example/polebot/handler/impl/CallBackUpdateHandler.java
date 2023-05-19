package com.example.polebot.handler.impl;

import com.example.polebot.converter.OggConverter;
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
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;

@Getter
@Setter
@Component
public class CallBackUpdateHandler implements UpdateHandler {

    @Autowired private MessageSender sender;
    @Autowired private OggConverter oggConverter;
    @Autowired private ChatGptService chatGptService;

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
            System.out.println("1: " + update.getMessage());
            System.out.println("2: " + update.getMessage().getVoice().getFileId());
            System.out.println("3: " + update.getMessage().getReplyToMessage());
            System.out.println("4: " + update.getMessage().getReplyToMessage().getVoice().getFileId());
            oggConverter.toMp3(update.getMessage().getReplyToMessage().getVoice().getFileId());
            String response = chatGptService.getVoiceTranscription();
            sender.sendMessage(response);
        } else if(callbackData.equals("Send to ChatGPT")) {
            oggConverter.toMp3(update.getMessage().getReplyToMessage().getVoice().getFileId());
            String transcription = chatGptService.getVoiceTranscription();
            String response = chatGptService.getChatGptResponse(transcription);
            sender.sendMessage(response);
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
