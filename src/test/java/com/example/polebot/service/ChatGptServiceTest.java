package com.example.polebot.service;

import com.example.polebot.service.impl.ChatGptService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ChatGptServiceTest {

    @Mock
    private ChatGptService chatGptService;

    private final String PROMPT = "simple prompt";

    @Test
    public void get_generated_image_url() {
        String expectedImageUrl = "imageUrl";
        Mockito.when(chatGptService.getGeneratedImageUrl(PROMPT)).thenReturn(expectedImageUrl);
        String actualImageUrl = chatGptService.getGeneratedImageUrl(PROMPT);
        assertEquals(expectedImageUrl, actualImageUrl);
    }

    @Test
    public void get_chat_gpt_response() {
        String expectedResponse = "chat gpt response";
        Mockito.when(chatGptService.getChatGptResponse(PROMPT)).thenReturn(expectedResponse);
        String actualResponse = chatGptService.getChatGptResponse(PROMPT);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void get_voice_transcription() {
        String expectedVoiceTranscription = "voice transcription";
        Mockito.when(chatGptService.getVoiceTranscription()).thenReturn(expectedVoiceTranscription);
        String actualVoiceTranscription = chatGptService.getVoiceTranscription();
        assertEquals(expectedVoiceTranscription, actualVoiceTranscription);
    }
}
