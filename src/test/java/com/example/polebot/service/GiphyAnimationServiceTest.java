package com.example.polebot.service;

import com.example.polebot.service.impl.GiphyAnimationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GiphyAnimationServiceTest {

    @Mock
    private GiphyAnimationService giphyAnimationService;

    @Test
    public void get_random_animation() {
        String expectedUrl = "animationUrl";
        Mockito.when(giphyAnimationService.getRandomAnimation("tag")).thenReturn(expectedUrl);
        String actualAnimationUrl = giphyAnimationService.getRandomAnimation("tag");
        assertEquals(expectedUrl, actualAnimationUrl);
    }
}
