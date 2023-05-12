package com.example.polebot.converter;

import org.springframework.stereotype.Component;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;

@Component
public class OggConverter {

    public void toMp3() {
        try{
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libmp3lame");
            audio.setBitRate(64000);
            audio.setChannels(2);
            audio.setSamplingRate(44100);

            //Encoding attributes
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setOutputFormat("mp3");
            attrs.setAudioAttributes(audio);

            //Encode
            Encoder encoder = new Encoder();
            File sourceFile = new File("./data/voices/savedVoice.ogg");
            File targetFile = new File("./data/voices/savedVoice.mp3");
            encoder.encode(new MultimediaObject(sourceFile), targetFile, attrs);
        } catch (IllegalArgumentException | EncoderException ex){

        }
    }

    public void create(String url, String fileName) {

    }
}
