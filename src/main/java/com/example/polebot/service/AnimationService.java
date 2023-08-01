package com.example.polebot.service;

import com.example.polebot.entity.Animation;

public interface AnimationService {

    String getRandomAnimation(String tag);

    String getAnimation();

    Animation saveAnimation(String id, String fileId, String name);
}
