package com.example.polebot.parser;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface Parser {
    void parse(Message message);
}
