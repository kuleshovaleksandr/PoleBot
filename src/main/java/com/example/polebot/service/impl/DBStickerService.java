package com.example.polebot.service.impl;

import com.example.polebot.entity.Sticker;
import com.example.polebot.repository.StickerRepository;
import com.example.polebot.service.StickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DBStickerService implements StickerService {

    @Autowired
    private StickerRepository stickerRepository;

    @Override
    public Sticker getStickerByEmoji(String emoji) {
        Sticker sticker = stickerRepository.findByEmoji(emoji).get();
        return sticker;
    }

    @Override
    public Sticker getStickerById(String id) {
        Sticker sticker = stickerRepository.findById(id).orElseThrow();
        return sticker;
    }

    @Override
    public Sticker saveSticker(String id, String fileId, String name, String emoji) {
        Sticker sticker = new Sticker();
        sticker.setId(id);
        sticker.setFileId(fileId);
        sticker.setName(name);
        sticker.setEmoji(emoji);
        stickerRepository.save(sticker);
        return sticker;
    }
}
