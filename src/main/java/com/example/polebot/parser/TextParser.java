package com.example.polebot.parser;

import com.example.polebot.handler.CallBackUpdateHandler;
import com.example.polebot.model.Currency;
import com.example.polebot.sender.Sender;
import com.example.polebot.service.CurrencyConversionService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TextParser implements Parser {

    @Autowired private Sender sender;
    @Autowired private CallBackUpdateHandler callBackUpdateHandler;
    @Autowired private CurrencyConversionService currencyConversionService;

    @Override
    public void parse(long chatId, String message) {
        getCurrencyValue(chatId, message);
    }

    private void findWordTesla(String message) {
        Pattern pattern = Pattern.compile("[Тт]есл.");
        Matcher matcher = pattern.matcher(message);
        while(matcher.find()) {
//            sendMessage(chatId, "you are boring");
        }
    }

    private void findWordKorea(String message) {

    }

    private void getCurrencyValue(long chatId, String message) {
        HashMap<String, Currency> currencyChoice = callBackUpdateHandler.getCurrencyChoice();
        if(currencyChoice.get("ORIGINAL") != null && currencyChoice.get("TARGET") != null) {
            double value;
            try {
                value = Double.parseDouble(message);
                Currency originalCurrency = currencyChoice.get("ORIGINAL");
                Currency targetCurrency = currencyChoice.get("TARGET");
                double conversionRatio = currencyConversionService.getConversionRatio(originalCurrency, targetCurrency);
                callBackUpdateHandler.init();
                String text = String.format("%4.2f %s is %4.2f %s", value, originalCurrency, (value * conversionRatio), targetCurrency);
                sender.sendMessage(chatId, text);
            } catch (NumberFormatException e) {
                sender.sendMessage(chatId,"Please enter a number");
            }
        }
    }
}
