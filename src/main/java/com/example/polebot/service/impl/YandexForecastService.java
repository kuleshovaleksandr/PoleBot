package com.example.polebot.service.impl;

import com.example.polebot.service.ForecastService;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class YandexForecastService implements ForecastService {
    @Value("${yandex.forecast.url}")
    private String YANDEX_URL;
    @Value("${yandex.forecast.key}")
    private String YANDEX_TOKEN;

    @SneakyThrows
    @Override
    public String getCurrentForecast() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(YANDEX_URL + "/?lat=53.9006&lon=27.5590")
                .get()
                .addHeader("X-Yandex-API-Key", YANDEX_TOKEN)
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
        return "";
    }
}
