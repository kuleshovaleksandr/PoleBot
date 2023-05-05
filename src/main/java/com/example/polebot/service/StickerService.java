package com.example.polebot.service;

import com.example.polebot.entity.Sticker;

public interface StickerService {
    Sticker getStickerByEmoji(String emoji);

    Sticker getStickerById(String id);

    void saveSticker(String id, String fileId, String name, String emoji);
}
