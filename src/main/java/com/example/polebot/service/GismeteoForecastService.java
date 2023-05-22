package com.example.polebot.service;

import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

@Service
public class GismeteoForecastService implements ForecastService {

    private final String GISMETEO_URL = "https://api.gismeteo.net/v2/weather/current";
    private final String GISMETEO_TOKEN = "56b30cb255.3443075";

    public void getCurrentForecast() {
        System.out.println(getResponse());
    }

    @SneakyThrows
    private String getResponse() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(GISMETEO_URL + "/?latitude=53.9006&longitude=27.5590")
                .get()
                .addHeader("X-Gismeteo-Token", GISMETEO_TOKEN)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
