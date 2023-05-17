package com.example.polebot.service;

import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import lombok.SneakyThrows;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Base64;
import java.util.List;

@Service
public class VoiceToTextService {

//    @Value("${open-ai.key}")
//    private String API_KEY;
    private static final String API_KEY = "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";
    private static final String ENGINE_ID = "ada";
    private static final String GPT_MODEL = "gpt-3.5-turbo";

    public void init() {
        String prompt = "tell me a joke about programmers";
        OpenAiService service = new OpenAiService(API_KEY, Duration.ofSeconds(30));
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(GPT_MODEL)
                .temperature(0.1)
                .messages(List.of(
                        new ChatMessage("system", "it's a system"),
                        new ChatMessage("user", prompt)))
                .build();
        StringBuilder builder = new StringBuilder();
        service.createChatCompletion(request).getChoices().forEach(choice -> builder.append(choice.getMessage().getContent()));
        String jsonResponse = builder.toString();

//        CompletionRequest completionRequest = CompletionRequest.builder()
//                .prompt("Somebody once told me the world is gonna roll me")
//                .model("ada")
//                .echo(true)
//                .build();
//        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);

    }

    @SneakyThrows
    public void getTextResponse() {

//        String endpoint = "https://api.openai.com/v1/engines/davinci/completions";
//        String prompt = "give me a joke";
//        String model = "davinci";
//        int maxTokens = 50;
//
//        String requestBody = "{\n"
//                + "  \"prompt\": \"" + prompt + "\",\n"
//                + "  \"max_tokens\": " + maxTokens + ",\n"
//                + "  \"model\": \"" + model + "\"\n"
//                + "}";
//        URL url = new URL(endpoint);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Content-Type", "application/json");
//        connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
//        connection.setDoOutput(true);
//
//        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
//        writer.write(requestBody);
//        writer.flush();
//        writer.close();
//
//        BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        StringBuilder responseBuilder = new StringBuilder();
//        String responseLine;
//        while ((responseLine = responseReader.readLine()) != null) {
//            responseBuilder.append(responseLine);
//        }
//        String response = responseBuilder.toString();
//
//        JsonParser parser = new JsonParser();
//        JsonObject responseObject = parser.parse(response).getAsJsonObject();
//        JsonArray choicesArray = responseObject.getAsJsonArray("choices");
//        JsonObject firstChoice = choicesArray.get(0).getAsJsonObject();
//        String textResponse = firstChoice.get("text").getAsString();
//
//        System.out.println(response);
    }

    @SneakyThrows
    public String transcribeVoiceMessage(String voiceFilePath) {
        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
        InputStream stream = new FileInputStream(voiceFilePath);

        recognizer.startRecognition(stream);
        SpeechResult result;
        while ((result = recognizer.getResult()) != null) {
            System.out.format("Hypothesis: %s\n", result.getHypothesis());
        }
        recognizer.stopRecognition();
        return result.getHypothesis();
    }
}
