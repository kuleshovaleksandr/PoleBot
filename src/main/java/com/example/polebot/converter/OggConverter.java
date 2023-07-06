package com.example.polebot.converter;

import com.example.polebot.PoleBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;

@Slf4j
@Component
public class OggConverter {

    @Autowired private PoleBot bot;
    @Value("${audio.ogg-path}")
    private String VOICE_OGG_PATH;
    @Value("${audio.mp3-path}")
    private String VOICE_MP3_PATH;

    public void toMp3(String fileId) {
        saveVoiceMessage(fileId);
        try{
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libmp3lame");
            audio.setBitRate(64000);
            audio.setChannels(2);
            audio.setSamplingRate(44100);

            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setOutputFormat("mp3");
            attrs.setAudioAttributes(audio);

            Encoder encoder = new Encoder();
            File sourceFile = new File(VOICE_OGG_PATH);
            File targetFile = new File(VOICE_MP3_PATH);
            encoder.encode(new MultimediaObject(sourceFile), targetFile, attrs);
            log.info("Voice message converted to MP3 with id: " + fileId);
        } catch (IllegalArgumentException | EncoderException e) {
            log.error("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveVoiceMessage(String fileId) {
        GetFile getFile = GetFile.builder()
                .fileId(fileId)
                .build();
        try {
            String filePath = bot.execute(getFile).getFilePath();
            File outputFile = new File(VOICE_OGG_PATH);
            bot.downloadFile(filePath, outputFile);
            log.info("Voice message saved with id: " + fileId);
        } catch(TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
