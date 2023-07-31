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
    private final String STICKER_FILE_ID = "CAACAgIAAxkBAAIBIGRU1UhDKSWqiBEiJR8KngiCc09RAAI2HQACeB5hSeLDi1LpC2yCLwQ";
    private final String STICKER_ID = "AgADNh0AAngeYUk";
    private final String STICKER_EMOJI = "\uD83E\uDE9F";
    private final String STICKER_NAME = "PoleShumit";

    @BeforeEach
    public void initSticker() {
        sticker = new Sticker(STICKER_ID,
                STICKER_FILE_ID,
                STICKER_NAME,
                STICKER_EMOJI);
    }

    @Test
    public void get_sticker_by_id() {
        Mockito.when(stickerRepository.findById(STICKER_ID)).thenReturn(Optional.of(sticker));
        Sticker sticker = stickerService.getStickerById(STICKER_ID);
        Assertions.assertEquals(STICKER_FILE_ID, sticker.getFileId());
        Assertions.assertEquals(STICKER_EMOJI, sticker.getEmoji());
    }

    @Test
    public void get_sticker_by_emoji() {
        Mockito.when(stickerRepository.findByEmoji(STICKER_EMOJI)).thenReturn(Optional.of(sticker));
        Sticker sticker = stickerService.getStickerByEmoji(STICKER_EMOJI);
        Assertions.assertEquals(STICKER_FILE_ID, sticker.getFileId());
        Assertions.assertEquals(STICKER_ID, sticker.getId());
    }

    @Test
    public void save_sticker_to_db() {
        Mockito.when(stickerRepository.save(sticker)).thenReturn(sticker);
        Sticker sticker = stickerService.saveSticker(STICKER_ID,
                STICKER_FILE_ID,
                STICKER_NAME,
                STICKER_EMOJI);
        Assertions.assertEquals(STICKER_ID, sticker.getId());
        Assertions.assertEquals(STICKER_FILE_ID, sticker.getFileId());
        Assertions.assertEquals(STICKER_NAME, sticker.getName());
        Assertions.assertEquals(STICKER_EMOJI, sticker.getEmoji());
    }
}
