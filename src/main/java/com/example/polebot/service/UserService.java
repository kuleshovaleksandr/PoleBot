package com.example.polebot.service;

import org.telegram.telegrambots.meta.api.objects.User;

public interface UserService {
    void registerUser(User user);
}
