package com.example.polebot.handler;

import com.example.polebot.model.Currency;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CallBackUpdateHandler implements UpdateHandler {
    @Override
    public void handleUpdate(Update update) {
//        CallbackQuery callbackQuery = update.getCallbackQuery();
//        String callbackData = callbackQuery.getData();
//        int messageId = callbackQuery.getMessage().getMessageId();
//        long chatId = callbackQuery.getMessage().getChatId();
//        String text = "";
//
//        if(callbackData.contains(":")) {
//            String[] param = callbackData.split(":");
//            String action = param[0];
//            Currency currency = Currency.valueOf(param[1]);
//            if(action.equals("ORIGINAL") || action.equals("TARGET")) {
//                currencyChoice.put(action, currency);
//            }
//        }
//
//        if(callbackData.equals("YES_BUTTON")) {
//            text = "You pressed Yes button";
//        } else if(callbackData.equals("NO_BUTTON")) {
//            text = "You pressed No button";
//        }
//        editText(chatId, messageId, text);
    }
}
