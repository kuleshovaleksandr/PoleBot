package com.example.polebot.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.SneakyThrows;
import org.apache.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;
import java.util.List;

@Service
public class ChatGptService {

    private static final String API_KEY = "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";
    private static final String GPT_MODEL = "gpt-3.5-turbo";
    private static final String VOICE_MODEL = "whisper-1";

    @SneakyThrows
    public String getChatGptResponse(String prompt) {
        URL url = new URL("https://api.openai.com/v1/chat/completions");
        StringBuilder response = new StringBuilder();
        String requestBody = "{\n" +
                "  \"model\": \"gpt-3.5-turbo\",\n" +
                "  \"messages\": [{\"role\": \"user\", \"content\": \"Tell me a joke about space\"}]\n" +
                "}\n";
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
        connection.setRequestProperty(HttpHeaders.ACCEPT, "application/json");
        connection.setRequestProperty(HttpHeaders.AUTHORIZATION, "Bearer " + API_KEY);
        connection.setDoOutput(true);

        OutputStream os = connection.getOutputStream();
        os.write(requestBody.getBytes());
        os.flush();

        try(BufferedReader responseReader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = responseReader.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response);
        }
        return response.toString();

//        OpenAiService service = new OpenAiService(API_KEY, Duration.ofSeconds(30));
//        ChatCompletionRequest request = ChatCompletionRequest.builder()
//                .model(GPT_MODEL)
//                .temperature(0.9)
//                .messages(List.of(
//                        new ChatMessage("system", "it's a system"),
//                        new ChatMessage("user", prompt)))
//                .build();
//        StringBuilder builder = new StringBuilder();
//        service.createChatCompletion(request).getChoices().forEach(choice -> builder.append(choice.getMessage().getContent()));
//        return builder.toString();
    }

    public String voiceToText() {
        return null;
    }

    @SneakyThrows
    public String getTranscription() {
        URL url = new URL("https://api.openai.com/v1/audio/transcriptions");
        File file = new File("./data/voices/savedVoice.mp3");
        StringBuilder response = new StringBuilder();

        String requestBody = "{\n"
                + "  \"file\": \"" + "./data/voices/savedVoice.mp3" + "\",\n"
                + "  \"model\": \"" + VOICE_MODEL + "\"\n"
                + "}";

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
//        connection.setRequestProperty(HttpHeaders.ACCEPT, "application/json");
        connection.setRequestProperty(HttpHeaders.AUTHORIZATION, "Bearer " + API_KEY);
        connection.setDoOutput(true);
        System.out.println(connection.getRequestProperties());
//        try(OutputStream writer = connection.getOutputStream()) {
//            byte[] input = requestBody.getBytes("utf-8");
//            writer.write(input, 0, input.length);
//        }

        OutputStream os = connection.getOutputStream();
        os.write(requestBody.getBytes());
        os.flush();

        try(BufferedReader responseReader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = responseReader.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response);
        }
        return response.toString();
    }
}
