package com.example.polebot.service;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface UserService {
    void registerUser(Message message);
}
