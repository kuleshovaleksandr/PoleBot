package com.example.polebot.handler;

import com.example.polebot.model.Currency;
import com.example.polebot.parser.CommandParser;
import com.example.polebot.parser.TextParser;
import com.example.polebot.sender.Sender;
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
