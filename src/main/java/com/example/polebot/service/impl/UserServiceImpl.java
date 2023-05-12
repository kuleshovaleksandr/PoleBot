package com.example.polebot.service.impl;

import com.example.polebot.entity.User;
import com.example.polebot.repository.UserRepository;
import com.example.polebot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.Timestamp;

@Service
public class UserServiceImpl implements UserService {

    @Autowired private UserRepository userRepository;

    @Override
    public void registerUser(Message message) {
        if(userRepository.findById(message.getChatId()).isEmpty()) {
            long id = message.getChatId();
            Chat chat = message.getChat();

            User user = new User();
            user.setId(id);
            user.setUserName(chat.getUserName());
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
        }
    }
}
