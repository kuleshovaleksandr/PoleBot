package com.example.polebot.service;

public interface AnimationService {

    String getRandomAnimation(String tag);

    String getAnimation();

    void saveAnimation(String id, String fileId, String name);
}
