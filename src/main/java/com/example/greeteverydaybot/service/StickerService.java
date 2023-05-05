package com.example.greeteverydaybot.service;

import com.example.greeteverydaybot.entity.Sticker;

public interface StickerService {
    Sticker getStickerByEmoji(String emoji);

    Sticker getStickerById(String id);

    void saveSticker(String id, String fileId, String name, String emoji);
}
