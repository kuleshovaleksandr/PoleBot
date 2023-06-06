package com.example.polebot.handler.impl;

import com.example.polebot.handler.UpdateHandler;
import com.example.polebot.parser.CommandParser;
import com.example.polebot.parser.TextParser;
import com.example.polebot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TextUpdateHandler implements UpdateHandler {

    @Autowired private TextParser textParser;
    @Autowired private CommandParser commandParser;
    @Autowired private UserService userService;

    @Override
    public void handleUpdate(Update update) {
        Message message = update.getMessage();
        userService.registerUser(update.getMessage());

        if(message.getText().startsWith("/")) {
            commandParser.parse(message);
        } else {
            textParser.parse(message);
        }
    }
}
