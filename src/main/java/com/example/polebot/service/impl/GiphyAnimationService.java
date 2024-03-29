package com.example.polebot.service.impl;

import com.example.polebot.entity.Animation;
import com.example.polebot.service.AnimationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class GiphyAnimationService implements AnimationService {

    private final String API_KEY = "RtrcIlEpP40CFYoqFX2cC805mczV5Gx2";
    private final String GIPHY_URL = "https://api.giphy.com/v1/gifs/";

    @Override
    public String getRandomAnimation(String tag) {
        String response = getResponse("random?api_key=" + API_KEY + "&tag=" + tag + "&rating=g");
        JSONObject json = new JSONObject(response);
        String gifId = json.getJSONObject("data").getString("id");
        return "https://i.giphy.com/" + gifId + ".gif";
    }

    @Override
    public String getAnimation() {
        String response = getResponse("");
        JSONObject json = new JSONObject(response);
        return null;
    }

    private String getResponse(String method) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(GIPHY_URL + method);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch(IOException e) {
            log.error("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return response.toString();
    }

    @Override
    public Animation saveAnimation(String id, String fileId, String name) {
        return null;
    }
}
