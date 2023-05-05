package com.example.greeteverydaybot.service;

import java.util.List;

public interface AnimationService {

    String getRandomGif(String tag);

    String searchGif();

    void saveAnimation(String id, String fileId, String name);
}
