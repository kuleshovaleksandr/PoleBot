package com.example.polebot.parser;

import com.example.polebot.handler.impl.CallBackUpdateHandler;
import com.example.polebot.model.Currency;
import com.example.polebot.sender.MessageSender;
import com.example.polebot.service.CurrencyConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TextParser implements Parser {

    @Autowired private MessageSender sender;
    @Autowired private CallBackUpdateHandler callBackUpdateHandler;
    @Autowired private CurrencyConversionService currencyConversionService;

    @Override
    public void parse(long chatId, String message) {
        sender.setChatId(chatId);
        getCurrencyValue(message);
        findWordTesla(message);
        findWordKorea(message);
    }

    private void findWordTesla(String message) {
        Pattern pattern = Pattern.compile("[Тт]есл.");
        Matcher matcher = pattern.matcher(message);
        while(matcher.find()) {
            sender.sendMessage("you typed \"Тесла\"");
        }
    }

    private void findWordKorea(String message) {
        Pattern pattern = Pattern.compile("[Кк]оре.");
        Matcher matcher = pattern.matcher(message);
        while(matcher.find()) {
            sender.sendMessage("you typed \"Корея\"");
        }
    }

    private void getCurrencyValue(String message) {
        HashMap<String, Currency> currencyChoice = callBackUpdateHandler.getCurrencyChoice();
        if(currencyChoice.get("ORIGINAL") != null && currencyChoice.get("TARGET") != null) {
            double value;
            try {
                value = Double.parseDouble(message);
                Currency originalCurrency = currencyChoice.get("ORIGINAL");
                Currency targetCurrency = currencyChoice.get("TARGET");
                double conversionRatio = currencyConversionService.getConversionRatio(originalCurrency, targetCurrency);
                callBackUpdateHandler.setDefaultValues();
                String text = String.format("%4.2f %s is %4.2f %s", value, originalCurrency, (value * conversionRatio), targetCurrency);
                sender.sendMessage(text);
            } catch (NumberFormatException e) {
                sender.sendMarkdownMessage("_Please enter a number_");
            }
        }
    }
}
