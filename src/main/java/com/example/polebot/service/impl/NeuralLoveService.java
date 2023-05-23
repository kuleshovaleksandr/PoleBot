package com.example.polebot.service.impl;

import lombok.SneakyThrows;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NeuralLoveService {

    @Value("${neural.love.token}")
    private String NEURAL_LOVE_TOKEN;
    @Value("${neural.love.generate-url}")
    private String NEURAL_LOVE_GENERATE_URL;
    @Value("${neural.love.result-url}")
    private String NEURAL_LOVE_RESULT_URL;
    @Value("${neural.love.price-url}")
    private String NEURAL_LOVE_PRICE_URL;

    private boolean imagesIsReady = false;
    private final Integer NUMBER_OF_IMAGES = 1;

    @SneakyThrows
    public List<String> generateImage(String prompt, String style, String layout) {
        imagesIsReady = false;
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("prompt", prompt);
        jsonRequest.put("style", style);
        jsonRequest.put("layout", layout);
        jsonRequest.put("amount", NUMBER_OF_IMAGES);
        jsonRequest.put("isHd", false);

        RequestBody requestBody = RequestBody.create(
                jsonRequest.toString(),
                MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(NEURAL_LOVE_GENERATE_URL)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + NEURAL_LOVE_TOKEN)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        JSONObject json = new JSONObject(response.body().string());
        String orderId = json.getString("orderId");
        System.out.println("orderId = " + orderId);
        return getImageResult(orderId);
    }

    @SneakyThrows
    private List<String> getImageResult(String orderId) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(NEURAL_LOVE_RESULT_URL + orderId)
                .get()
                .addHeader("Authorization", "Bearer " + NEURAL_LOVE_TOKEN)
                .addHeader("Accept", "application/json")
                .build();

        JSONObject json = null;

        while(!imagesIsReady) {
            Thread.sleep(5000);
            Response response = client.newCall(request).execute();
            json = new JSONObject(response.body().string());
            System.out.println(json);
            JSONObject status = json.getJSONObject("status");
            imagesIsReady = status.getBoolean("isReady");
        }

        JSONArray outputArray = json.getJSONArray("output");
        List<String> imageUrlList = new ArrayList();

        if(outputArray != null) {
            for(int i = 0; i < outputArray.length(); i++) {
                JSONObject output = outputArray.getJSONObject(i);
                String imageUrl = output.getString("full");
                imageUrlList.add(imageUrl);
            }
        }
        return imageUrlList;
    }
}
