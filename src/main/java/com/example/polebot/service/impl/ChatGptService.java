package com.example.polebot.service.impl;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.SneakyThrows;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Duration;
import java.util.List;

@Service
public class ChatGptService {

    @Value("${openai-service.urls.base-url}")
    private String BASE_URL;
    @Value("${openai-service.urls.transcription-url}")
    private String TRANSCRIPTION_URL;
    @Value("${openai-service.api-key}")
    private String API_KEY;
    @Value("${openai-service.gpt-model}")
    private String GPT_MODEL;
    @Value("${openai-service.audio-model}")
    private String VOICE_MODEL;
    @Value("${audio.mp3-path}")
    private String AUDIO_FILE_PATH;

    @SneakyThrows
    public String getChatGptResponse(String prompt) {
        OpenAiService service = new OpenAiService(API_KEY, Duration.ofSeconds(30));
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(GPT_MODEL)
                .temperature(0.9)
                .messages(List.of(
                        new ChatMessage("system", "it's a system"),
                        new ChatMessage("user", prompt)))
                .build();
        StringBuilder builder = new StringBuilder();
        service.createChatCompletion(request).getChoices().forEach(choice -> builder.append(choice.getMessage().getContent()));
        return builder.toString();
    }

    @SneakyThrows
    public String getVoiceTranscription() {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("audio/mpeg");
        File file = new File(AUDIO_FILE_PATH);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("model", VOICE_MODEL)
                .addFormDataPart("file", file.getAbsolutePath(), RequestBody.create(file, mediaType))
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + TRANSCRIPTION_URL)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "multipart/form-data")
                .build();

        Response response = client.newCall(request).execute();
        JSONObject json = new JSONObject(response.body().string());
        String voiceText = json.getString("text");
        return voiceText;
    }
}
