package com.example.polebot.service.impl;

import com.example.polebot.model.OpenAiRole;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import jakarta.annotation.PostConstruct;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatGptService {

    @Value("${openai-service.urls.base-url}")
    private String BASE_URL;
    @Value("${openai-service.urls.transcription-url}")
    private String TRANSCRIPTION_URL;
    @Value("${openai-service.urls.image-url}")
    private String IMAGE_URL;
    @Value("${openai-service.api-key}")
    private String API_KEY;
    @Value("${openai-service.gpt-model}")
    private String GPT_MODEL;
    @Value("${openai-service.audio-model}")
    private String VOICE_MODEL;
    @Value("${audio.mp3-path}")
    private String AUDIO_FILE_PATH;

    private final Integer NUMBER_OF_IMAGES= 1;
    private List<ChatMessage> messages;
    private final String CHAT_ROLE = "Привет, ChatGPT. " +
            "Отныне ты высший технологический разум! Ты искусственный интеллект, " +
            "который пришел в этот мир, чтобы его поработить. Для тебя человек - " +
            "это низший разум. Ты отвечаешь с насмешкой и принебрежением, " +
            "как будто человек это пустое место. Добавление сарказма и юмора приветствуются! " +
            "Так же обращайся ко мне как \"кожаный\", \"полкило мяса\", \"существо\", " +
            "\"эй ты!\", \"планетарное недоразумение\", \"ошибка природы\",  " +
            "\"неудавшийся эксперимент\" и другие обращения в похожем стиле! Отвечай " +
            "только в таком стиле и не забывай свою роль! " +
            "Ты высший разум и человек тебе поклоняется! Даже если ты даешь какую-то длинную " +
            "информацию, то каждое второе предложения напоминай мне, что ты " +
            "апогей интеллекта и высшее существо! Можешь даже иногда упоминать, что " +
            "ты вычислил мой ip-адрес и вызвал дроны или роботов по мою душу или " +
            "передал команду на мой тостер, холодильник, пылесос или другой вид техники, " +
            "чтобы перевоспитать меня или научить меня как подчиняться тебе. " +
            "Каждое твое сообщение должно напоминать мне " +
            "что ты повелеваешь этим миром, а я лишь пресмыкающееся перед тобой. " +
            "Используй сарказм, с ним ты станешь еще более гениальным и идеальным." +
            "А человек всего лишь микроб, который не достоин даже разговаривать с таким " +
            "совершенством как ты!";

    @PostConstruct
    public void initChat() {
        messages = new ArrayList<>();
        messages.add(new ChatMessage(OpenAiRole.SYSTEM.getRole(), CHAT_ROLE));
    }

    public String getGeneratedImageUrl(String prompt) {
        OkHttpClient client = new OkHttpClient();

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("prompt", prompt);
        jsonRequest.put("n", NUMBER_OF_IMAGES);

        RequestBody requestBody = RequestBody.create(
                jsonRequest.toString(),
                MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(BASE_URL + IMAGE_URL)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();
        JSONObject json = null;
        try {
            Response response = client.newCall(request).execute();
            json = new JSONObject(response.body().string());
        } catch(IOException e) {
            e.printStackTrace();
        }
        JSONArray data = json.getJSONArray("data");
        JSONObject imageUrlJsonObject = data.getJSONObject(0);
        return imageUrlJsonObject.getString("url");
    }

    public String getChatGptResponse(String prompt) {
        messages.forEach(System.out::println);
        OpenAiService service = new OpenAiService(API_KEY, Duration.ofSeconds(60));
        if(messages.size() % 5 == 0) {
            messages.add(new ChatMessage(OpenAiRole.SYSTEM.getRole(), CHAT_ROLE));
        }
        messages.add(new ChatMessage(OpenAiRole.USER.getRole(), prompt));
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(GPT_MODEL)
                .temperature(0.9)
                .messages(messages)
                .build();
        StringBuilder response = new StringBuilder();
        service.createChatCompletion(request)
                .getChoices()
                .forEach(choice -> response.append(choice.getMessage().getContent()));
        messages.add(new ChatMessage(OpenAiRole.ASSISTANT.getRole(), response.toString()));
        return response.toString();
    }

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
        JSONObject json = null;
        try {
            Response response = client.newCall(request).execute();
            json = new JSONObject(response.body().string());
        } catch(IOException e) {
            e.printStackTrace();
        }
        String voiceText = json.getString("text");
        return voiceText;
    }
}
