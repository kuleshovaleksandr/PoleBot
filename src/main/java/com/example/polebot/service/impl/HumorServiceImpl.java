package com.example.polebot.service.impl;

import com.example.polebot.service.HumorService;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class HumorServiceImpl implements HumorService {

    @Value("${humor.api.url}")
    private String BASE_API_URL;
    @Value("${humor.api.key}")
    private String API_KEY;

    private final String MIN_RATING = "8";
    private final String NUMBER_OF_JOKES = "1";

    @Override
    public String searchJoke(String request) {
        HttpUrl httpUrl = HttpUrl.parse(BASE_API_URL + "jokes/search").newBuilder()
                .addQueryParameter("api-key", API_KEY)
                .addQueryParameter("min-rating", MIN_RATING)
                .addQueryParameter("number", NUMBER_OF_JOKES)
                .addQueryParameter("keywords", parseRequestToKeywords(request))
                .build();
        JSONObject json = getJsonResponse(httpUrl);
        JSONArray jsonArray = json.getJSONArray("jokes");
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        String joke = jsonObject.getString("joke");
        return joke;
    }

    @Override
    public String getRandomJoke() {
        HttpUrl httpUrl = HttpUrl.parse(BASE_API_URL + "jokes/random").newBuilder()
                .addQueryParameter("api-key", API_KEY)
                .addQueryParameter("min-rating", MIN_RATING)
                .build();
        JSONObject json = getJsonResponse(httpUrl);
        String joke = json.getString("joke");
        return joke;
    }

    @Override
    public String searchMeme(String request) {
        HttpUrl httpUrl = HttpUrl.parse(BASE_API_URL + "memes/search").newBuilder()
                .addQueryParameter("api-key", API_KEY)
                .addQueryParameter("min-rating", MIN_RATING)
                .addQueryParameter("number", NUMBER_OF_JOKES)
                .addQueryParameter("keywords", parseRequestToKeywords(request))
                .build();
        JSONObject json = getJsonResponse(httpUrl);
        return null;
    }

    @Override
    public String getRandomMeme() {
        HttpUrl httpUrl = HttpUrl.parse(BASE_API_URL + "memes/random").newBuilder()
                .addQueryParameter("api-key", API_KEY)
                .addQueryParameter("min-rating", MIN_RATING)
                .build();
        JSONObject json = getJsonResponse(httpUrl);
        return null;
    }

    private JSONObject getJsonResponse(HttpUrl httpUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("X-API-Quota-Request", "The number of points used by the request")
                .addHeader("X-API-Quota-Used", "The number of points used in total today")
                .build();
        Response response;
        JSONObject json = null;
        try {
            response = client.newCall(request).execute();
            json = new JSONObject(response.body().string());
        } catch(IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    private String parseRequestToKeywords(String request) {
        return request;
    }
}
