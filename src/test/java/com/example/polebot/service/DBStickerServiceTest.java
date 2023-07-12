package com.example.polebot.service;

import com.example.polebot.entity.Sticker;
import com.example.polebot.repository.StickerRepository;
import com.example.polebot.service.impl.DBStickerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DBStickerServiceTest {

    @InjectMocks
    private DBStickerService stickerService;

    @Mock
    private StickerRepository stickerRepository;

    private Sticker sticker;

    @BeforeEach
    public void initSticker() {
        sticker = new Sticker("AgADNh0AAngeYUk",
                "CAACAgIAAxkBAAIBIGRU1UhDKSWqiBEiJR8KngiCc09RAAI2HQACeB5hSeLDi1LpC2yCLwQ",
                "PoleShumit",
                "\uD83E\uDE9F");
    }

    @Test
    public void getStickerById() {
        Mockito.when(stickerRepository.findById("AgADNh0AAngeYUk")).thenReturn(Optional.of(sticker));
        Sticker sticker = stickerService.getStickerById("AgADNh0AAngeYUk");
        Assertions.assertEquals("CAACAgIAAxkBAAIBIGRU1UhDKSWqiBEiJR8KngiCc09RAAI2HQACeB5hSeLDi1LpC2yCLwQ",
                sticker.getFileId());
        Assertions.assertEquals("\uD83E\uDE9F", sticker.getEmoji());
    }

    @Test
    public void getStickerByEmoji() {
        Mockito.when(stickerRepository.findByEmoji("\uD83E\uDE9F")).thenReturn(Optional.of(sticker));
        Sticker sticker = stickerService.getStickerByEmoji("\uD83E\uDE9F");
        Assertions.assertEquals("CAACAgIAAxkBAAIBIGRU1UhDKSWqiBEiJR8KngiCc09RAAI2HQACeB5hSeLDi1LpC2yCLwQ",
                sticker.getFileId());
        Assertions.assertEquals("AgADNh0AAngeYUk",
                sticker.getId());
    }

    @Test
    public void saveStickerToDB() {
        Mockito.when(stickerRepository.save(sticker)).thenReturn(sticker);
        Sticker sticker = stickerService.saveSticker("AgADNh0AAngeYUk",
                "CAACAgIAAxkBAAIBIGRU1UhDKSWqiBEiJR8KngiCc09RAAI2HQACeB5hSeLDi1LpC2yCLwQ",
                "PoleShumit",
                "\uD83E\uDE9F");
        Assertions.assertEquals("AgADNh0AAngeYUk",
                sticker.getId());
        Assertions.assertEquals("CAACAgIAAxkBAAIBIGRU1UhDKSWqiBEiJR8KngiCc09RAAI2HQACeB5hSeLDi1LpC2yCLwQ",
                sticker.getFileId());
        Assertions.assertEquals("PoleShumit",
                sticker.getName());
        Assertions.assertEquals("\uD83E\uDE9F",
                sticker.getEmoji());
    }
}
