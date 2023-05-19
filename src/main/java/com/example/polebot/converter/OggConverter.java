package com.example.polebot.converter;

import com.example.polebot.PoleBot;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;

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
        } catch (IllegalArgumentException | EncoderException ex){

        }
    }

    @SneakyThrows
    private void saveVoiceMessage(String fileId) {
        GetFile getFile = GetFile.builder()
                .fileId(fileId)
                .build();
        String filePath = bot.execute(getFile).getFilePath();
        File outputFile = new File(VOICE_OGG_PATH);
        bot.downloadFile(filePath, outputFile);
    }
}
