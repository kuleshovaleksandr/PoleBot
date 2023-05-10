package com.example.polebot.parser;

import com.example.polebot.model.Currency;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TextParser {

    public void findWordTesla(String message) {
        Pattern pattern = Pattern.compile("[Тт]есл.");
        Matcher matcher = pattern.matcher(message);
        while(matcher.find()) {
//            sendMessage(chatId, "you are boring");
        }
    }

    public void findWordKorea(String message) {

    }

    public String getCurrencyValue(String message) {

        return null;
    }

    public HashMap<String, Currency> getCurrencyChoice() {
//        if(currencyChoice.get("ORIGINAL") != null && currencyChoice.get("TARGET") != null) {
//            double value = 0;
//            try {
//                value = Double.parseDouble(messageText);
//                Currency originalCurrency = currencyChoice.get("ORIGINAL");
//                Currency targetCurrency = currencyChoice.get("TARGET");
//                double conversionRatio = currencyConversionService.getConversionRatio(originalCurrency, targetCurrency);
//
//                SendMessage message = SendMessage.builder()
//                        .chatId(chatId)
//                        .text(String.format(
//                                "%4.2f %s is %4.2f %s",
//                                value, originalCurrency, (value * conversionRatio), targetCurrency))
//                        .build();
//                init();
//                execute(message);
//            } catch (NumberFormatException e) {
//                SendMessage message = SendMessage.builder()
//                        .chatId(chatId)
//                        .text("Please enter a number")
//                        .build();
//                execute(message);
//            }
//        }
        return null;
    }
}
