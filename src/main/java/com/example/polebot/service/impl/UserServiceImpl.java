package com.example.polebot.service.impl;

import com.example.polebot.entity.User;
import com.example.polebot.repository.UserRepository;
import com.example.polebot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class UserServiceImpl implements UserService {

    @Autowired private UserRepository userRepository;

    @Override
    public void registerUser(org.telegram.telegrambots.meta.api.objects.User chatUser) {
        if(userRepository.findById(chatUser.getId()).isEmpty()) {
            User user = new User();
            user.setId(chatUser.getId());
            user.setUserName(chatUser.getUserName());
            user.setFirstName(chatUser.getFirstName());
            user.setLastName(chatUser.getLastName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
        }
    }
}
