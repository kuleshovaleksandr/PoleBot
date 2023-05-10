package com.example.polebot.handler;

import com.example.polebot.model.Currency;
import com.example.polebot.service.StickerService;
import com.example.polebot.service.impl.DBAnimationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.games.Animation;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

@Service
public class UpdateHandlerImpl implements UpdateHandler {

    @Autowired
    private StickerService stickerService;
    @Autowired
    private DBAnimationService dbAnimationService;

    @Override
    public void handleSticker(Sticker sticker) {
        String id = sticker.getFileUniqueId();
        String fileId = sticker.getFileId();
        String name = sticker.getSetName();
        String emoji = sticker.getEmoji();
        stickerService.saveSticker(id, fileId, name, emoji);
    }

    @Override
    public void handleCallBack(CallbackQuery callbackQuery) {
//        String callbackData = callbackQuery.getData();
//        int messageId = callbackQuery.getMessage().getMessageId();
//        long chatId = callbackQuery.getMessage().getChatId();
//        String text = "";
//
//        if(callbackData.contains(":")) {
//            String[] param = callbackData.split(":");
//            String action = param[0];
//            Currency currency = Currency.valueOf(param[1]);
//            if(action.equals("ORIGINAL") || action.equals("TARGET")) {
//                currencyChoice.put(action, currency);
//            }
//        }
//
//        if(callbackData.equals("YES_BUTTON")) {
//            text = "You pressed Yes button";
//        } else if(callbackData.equals("NO_BUTTON")) {
//            text = "You pressed No button";
//        }
//        editText(chatId, messageId, text);
    }

    @Override
    public void handleAnimation(Animation animation) {
        String id = animation.getFileUniqueId();
        String fileId = animation.getFileId();
        String name = animation.getFileName();
        dbAnimationService.saveAnimation(id, fileId, name);
    }
}
