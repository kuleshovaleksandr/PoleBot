package com.example.polebot.downloader;

import com.example.polebot.PoleBot;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;

import java.io.File;

@Component
public class FileDownloader implements Downloader {

    @Autowired private PoleBot bot;
    private final String VOICE_DOWNLOAD_PATH = "./data/voices/savedVoice.ogg";

    @SneakyThrows
    @Override
    public void downloadVoice(String fileId) {
        GetFile getFile = GetFile.builder()
                .fileId(fileId)
                .build();
        String filePath = bot.execute(getFile).getFilePath();
        java.io.File outputFile = new File(VOICE_DOWNLOAD_PATH);
        bot.downloadFile(filePath, outputFile);
    }
}
