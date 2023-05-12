package com.example.polebot.handler.impl;

import com.example.polebot.handler.UpdateHandler;
import com.example.polebot.service.StickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

@Component
public class StickerUpdateHandler implements UpdateHandler {

    @Autowired private StickerService stickerService;

    @Override
    public void handleUpdate(Update update) {
        Sticker sticker = update.getMessage().getSticker();
        String id = sticker.getFileUniqueId();
        String fileId = sticker.getFileId();
        String name = sticker.getSetName();
        String emoji = sticker.getEmoji();
        stickerService.saveSticker(id, fileId, name, emoji);
    }
}
