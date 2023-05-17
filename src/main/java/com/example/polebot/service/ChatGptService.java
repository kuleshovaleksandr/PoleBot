package com.example.polebot.service;

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
import java.util.Base64;

@Service
public class ChatGptService {

    private static final String API_KEY = "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";
    private static final String GPT_MODEL = "gpt-3.5-turbo";
    private static final String VOICE_MODEL = "whisper-1";

    @SneakyThrows
    public String getChatGptResponse(String prompt) {
        URL url = new URL("https://api.openai.com/v1/chat/completions");
        String requestBody = "{\n" +
                "  \"model\": \"" + GPT_MODEL + "\",\n" +
                "  \"messages\": [{\"role\": \"user\", \"content\": \"Tell me a joke about space\"}]\n" +
                "}\n";

        return getResponse(requestBody, url);

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

    @SneakyThrows
    public String getTranscription() {
        URL url = new URL("https://api.openai.com/v1/audio/transcriptions");
        File file = new File("./data/voices/savedVoice.mp3");
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        String audioBase64 = Base64.getEncoder().encodeToString(fileBytes);
        String requestBody = "{\n"
                + "  \"file\": \"" + audioBase64 + "\",\n"
                + "  \"model\": \"" + VOICE_MODEL + "\"\n"
                + "}";
        return getResponse(requestBody, url);
    }

    @SneakyThrows
    private String getResponse(String requestBody, URL url) {
        StringBuilder response = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
//        connection.setRequestProperty(HttpHeaders.ACCEPT, "application/json");
        connection.setRequestProperty(HttpHeaders.AUTHORIZATION, "Bearer " + API_KEY);
        connection.setDoOutput(true);
        connection.setDoInput(true);
//        try(OutputStream writer = connection.getOutputStream()) {
//            byte[] input = requestBody.getBytes("utf-8");
//            writer.write(input, 0, input.length);
//        }

        OutputStream os = connection.getOutputStream();
        os.write(requestBody.getBytes());
        os.flush();
        os.close();

        try(BufferedReader responseReader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = responseReader.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        return response.toString();
    }
}
