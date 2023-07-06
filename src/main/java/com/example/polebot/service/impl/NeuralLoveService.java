package com.example.polebot.service.impl;

import com.example.polebot.exception.ConnectionTimeOutException;
import com.example.polebot.model.NeuralLoveArtLayout;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class NeuralLoveService {

    @Value("${neural.love.token}")
    private String NEURAL_LOVE_TOKEN;
    @Value("${neural.love.generate-url}")
    private String NEURAL_LOVE_GENERATE_URL;
    @Value("${neural.love.result-url}")
    private String NEURAL_LOVE_RESULT_URL;

    private boolean imageIsReady = false;
    private final Integer NUMBER_OF_IMAGES = 1;

    public List<String> generateImage(String prompt, String style) throws ConnectionTimeOutException {
        imageIsReady = false;
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("prompt", prompt);
        jsonRequest.put("style", style);
        jsonRequest.put("layout", NeuralLoveArtLayout.SQUARE.getLayout());
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

        String orderId = null;
        try {
            Response response = client.newCall(request).execute();
            JSONObject json = new JSONObject(response.body().string());
            orderId = json.getString("orderId");
        } catch(IOException e) {
            log.error("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return getImageResult(orderId);
    }

    private List<String> getImageResult(String orderId) throws ConnectionTimeOutException {
        int connectionCount = 0;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(NEURAL_LOVE_RESULT_URL + orderId)
                .get()
                .addHeader("Authorization", "Bearer " + NEURAL_LOVE_TOKEN)
                .addHeader("Accept", "application/json")
                .build();

        JSONArray outputArray = null;

        do {
            if (++connectionCount > 10) {
                throw new ConnectionTimeOutException("Server does not response. Try again or later.");
            }
            try {
                Thread.sleep(6000);
                Response response = client.newCall(request).execute();
                JSONObject json = new JSONObject(response.body().string());
                JSONObject status = json.getJSONObject("status");
                outputArray = json.getJSONArray("output");
                imageIsReady = status.getBoolean("isReady");
            } catch(InterruptedException | IOException e) {
                e.printStackTrace();
            }
        } while(!imageIsReady && outputArray.isEmpty());

        ArrayList imageUrlList = new ArrayList();

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
