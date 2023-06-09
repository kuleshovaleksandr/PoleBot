package com.example.polebot.parser;

import com.example.polebot.handler.impl.CallBackUpdateHandler;
import com.example.polebot.model.Currency;
import com.example.polebot.sender.MessageSender;
import com.example.polebot.service.CurrencyConversionService;
import com.example.polebot.service.impl.ChatGptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class TextParser implements Parser {

    @Autowired private MessageSender sender;
    @Autowired private ChatGptService chatGptService;
    @Autowired private CallBackUpdateHandler callBackUpdateHandler;
    @Autowired private CurrencyConversionService currencyConversionService;

    private final String TESLA_REQUEST = "расскажи смешную шутку про автомобиль тесла";
    private final String KOREA_REQUEST = "расскажи интересный факт про Корею";

    @Override
    public void parse(Message message) {
        sender.setChatId(message.getChatId());
        String text = message.getText();
        getCurrencyValue(text);
//        findWordTesla(text);
//        findWordKorea(text);
    }

    private void findWordTesla(String message) {
        Pattern pattern = Pattern.compile("[Тт]есл.");
        Matcher matcher = pattern.matcher(message);
        while(matcher.find()) {
            sender.sendMarkdownMessage("*ChatGPT*: " +
                    "_" + chatGptService.getChatGptResponse(TESLA_REQUEST) + "_");
        }
    }

    private void findWordKorea(String message) {
        Pattern pattern = Pattern.compile("[Кк]оре.");
        Matcher matcher = pattern.matcher(message);
        while(matcher.find()) {
            sender.sendMarkdownMessage("*ChatGPT*: " +
                    "_" + chatGptService.getChatGptResponse(KOREA_REQUEST) + "_");
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
                log.error("Number format exception trying parse " + message + " to Double");
                sender.sendMarkdownMessage("_Please enter a number_");
            }
        }
    }
}
