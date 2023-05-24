package com.example.polebot.service;

public interface HumorService {

    String searchJoke(String request);

    String getRandomJoke();

    String searchMeme(String request);

    String getRandomMeme();
}
