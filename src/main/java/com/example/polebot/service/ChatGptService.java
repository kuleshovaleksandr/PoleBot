package com.example.polebot.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class ChatGptService {

    private static final String API_KEY = "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";
    private static final String GPT_MODEL = "gpt-3.5-turbo";

    public String getChatGptResponse(String prompt) {
        OpenAiService service = new OpenAiService(API_KEY, Duration.ofSeconds(30));
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(GPT_MODEL)
                .temperature(0.8)
                .messages(List.of(
                        new ChatMessage("system", "it's a system"),
                        new ChatMessage("user", prompt)))
                .build();
        StringBuilder builder = new StringBuilder();
        service.createChatCompletion(request).getChoices().forEach(choice -> builder.append(choice.getMessage().getContent()));
        String jsonResponse = builder.toString();
        System.out.println(jsonResponse);
        return jsonResponse;
    }
}
