package com.example.polebot.handler;

import com.example.polebot.parser.CommandParser;
import com.example.polebot.parser.TextParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TextUpdateHandler implements UpdateHandler {

    @Autowired private TextParser textParser;
    @Autowired private CommandParser commandParser;

    @Override
    public void handleUpdate(Update update) {
        String message = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        if(message.startsWith("/")) {
            commandParser.parse(chatId, message);
        } else {
            textParser.parse(chatId, message);
        }
    }
}
