package com.example.polebot.service;

import com.example.polebot.exception.ConnectionTimeOutException;
import com.example.polebot.service.impl.NeuralLoveService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class NeuralLoveServiceTest {

    @Mock
    private NeuralLoveService neuralLoveService;

    @Test
    public void get_generated_images() throws ConnectionTimeOutException {
        List<String> images = List.of("image1", "image2", "image3");
        Mockito.when(neuralLoveService.generateImage("prompt", "style"))
                .thenReturn(images)
                .thenThrow(ConnectionTimeOutException.class);
        assertEquals(images, neuralLoveService.generateImage("prompt", "style"));
        assertThrows(ConnectionTimeOutException.class, () -> neuralLoveService.generateImage("prompt", "style"));
    }
}
