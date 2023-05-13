package com.example.polebot.service;

import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;
import lombok.SneakyThrows;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

@Service
public class VoiceToTextService {

//    @Value("${open-ai.key}")
//    private String API_KEY;
    private static final String API_KEY = "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";
    private static final String ENGINE_ID = "ada";

    public void init() {
        OpenAiService service = new OpenAiService(API_KEY);
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("Somebody once told me the world is gonna roll me")
                .model("ada")
                .echo(true)
                .build();
        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);

    }

    @SneakyThrows
    public String transcribeVoiceMessage(String voiceFilePath) {
//        try {
//            File voiceFile = new File(voiceFilePath);
//            BufferedReader reader = new BufferedReader(new FileReader(voiceFile));
//            StringBuilder stringBuilder = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                stringBuilder.append(line);
//            }
//            String voiceMessage = stringBuilder.toString();
//
//            String voiceMessageBase64 = Base64.getEncoder().encodeToString(voiceMessage.getBytes());
//
//            URL url = new URL("https://api.openai.com/v1/completions");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
//            connection.setDoOutput(true);
//
//            String requestBody = "{"
//                    + "\"engine\": \"" + ENGINE_ID + "\","
//                    + "\"prompt\": \"Transcribe the following voice message: " + voiceFile.getName() + "\","
//                    + "\"file\": \"" + voiceMessageBase64 + "\""
//                    + "}";
//
//            System.out.println(requestBody);
//
//            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
//            writer.write(requestBody);
//            writer.flush();
//            writer.close();
//
//            BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            StringBuilder responseBuilder = new StringBuilder();
//            String responseLine;
//            while ((responseLine = responseReader.readLine()) != null) {
//                responseBuilder.append(responseLine);
//            }
//            String response = responseBuilder.toString();
//            System.out.println(response);
//            JSONObject jsonResponse = new JSONObject(response);
//            String transcription = jsonResponse.getJSONArray("choices").getJSONObject(0).getString("text");
//
//            return transcription;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        byte[] voiceData = Files.readAllBytes(new File(voiceFilePath).toPath());
//        OkHttpClient client = new OkHttpClient();
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("engine", ENGINE_ID)
//                .addFormDataPart("prompt", "Transcribe the following voice message:")
//                .addFormDataPart("file", "savedVoice.mp3", RequestBody.create(MediaType.parse("application/octet-stream"), voiceData))
//                .build();
//        Request request = new Request.Builder()
//                .url("https://api.openai.com/v1/files/gpt")
//                .header("Authorization", "Bearer " + API_KEY)
//                .post(requestBody)
//                .build();
//        Response response = client.newCall(request).execute();
//        String responseJson = response.body().string();
//        System.out.println(responseJson);
//        String transcription = responseJson.split("\"text\": \"")[1].split("\"")[0];

        OpenAiService service = new OpenAiService(API_KEY);
//        File file = new File(voiceFilePath);
        CompletionRequest completionRequest = CompletionRequest.builder()
//                .prompt("Transcribe the following voice message: " + file.getName())
                .prompt("tell me a joke about programmers")
                .model("ada")
                .echo(true)
                .build();
        CompletionResult result = service.createCompletion(completionRequest);
        List<CompletionChoice> choices = result.getChoices();
        choices.forEach(System.out::println);

//        APIRequest apiRequest = new APIRequest(API_KEY);
//        FileReference fileReference = new FileReference(voiceFilePath);
//        CompletionRequest request = new CompletionRequest.Builder()
//                .engine(ENGINE_ID)
//                .prompt("Transcribe the following voice message: " + fileReference.getUrl())
//                .file(fileReference)
//                .build();
//        APIResponse apiResponse = apiRequest.getApi().createCompletion(request);
//        String transcription = apiResponse.getChoices().get(0).getText();
        return "transcription";
    }
}
