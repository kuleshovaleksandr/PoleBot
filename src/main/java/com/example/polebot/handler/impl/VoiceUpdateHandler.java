package com.example.polebot.handler.impl;

import com.example.polebot.converter.OggConverter;
import com.example.polebot.handler.UpdateHandler;
import com.example.polebot.sender.MessageSender;
import com.example.polebot.service.impl.ChatGptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class VoiceUpdateHandler implements UpdateHandler {

    @Autowired private MessageSender sender;
    @Autowired private OggConverter oggConverter;
    @Autowired private ChatGptService chatGptService;

    @Override
    public void handleUpdate(Update update) {
        oggConverter.toMp3(update.getMessage().getVoice().getFileId());
        String response = chatGptService.getVoiceTranscription();
        sender.sendMessage(response);
    }
}
