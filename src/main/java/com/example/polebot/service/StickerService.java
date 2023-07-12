package com.example.polebot.service;

import com.example.polebot.entity.Sticker;

public interface StickerService {
    Sticker getStickerByEmoji(String emoji);

    Sticker getStickerById(String id);

    Sticker saveSticker(String id, String fileId, String name, String emoji);
}
