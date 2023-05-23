package com.example.polebot.service.impl;

import com.example.polebot.service.ForecastService;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GismeteoForecastService implements ForecastService {

    @Value("${gismeteo.forecast.url}")
    private String GISMETEO_URL;

    private String GISMETEO_TOKEN;

    public String getCurrentForecast() {
        System.out.println(getResponse());
        return "";
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
