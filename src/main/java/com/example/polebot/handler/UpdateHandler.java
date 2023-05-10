package com.example.polebot.handler;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.games.Animation;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

public interface UpdateHandler {
    void handleSticker(Sticker sticker);

    void handleCallBack(CallbackQuery callbackQuery);

    void handleAnimation(Animation animation);
}
