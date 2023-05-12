package com.example.polebot.handler.impl;

import com.example.polebot.converter.OggConverter;
import com.example.polebot.downloader.Downloader;
import com.example.polebot.handler.UpdateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class VoiceUpdateHandler implements UpdateHandler {

//    @Autowired private Downloader downloader;
    @Autowired private OggConverter converter;

    @Override
    public void handleUpdate(Update update) {
//        downloader.downloadVoice(update.getMessage().getVoice().getFileId());
        converter.create(update.getMessage().getVoice().getFileId());
        converter.toMp3();
    }
}
