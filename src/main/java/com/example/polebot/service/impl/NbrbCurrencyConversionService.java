package com.example.polebot.service.impl;

import com.example.polebot.entity.CurrencyRate;
import com.example.polebot.model.Currency;
import com.example.polebot.repository.CurrencyRateRepository;
import com.example.polebot.service.CurrencyConversionService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;

@Slf4j
@Service
public class NbrbCurrencyConversionService implements CurrencyConversionService {

    @Autowired private CurrencyRateRepository currencyRateRepository;

    private final String NBRB_URL = "https://www.nbrb.by/api/exrates/rates/";

    @Override
    public double getConversionRatio(Currency original, Currency target) {
        double originalRate = getRate(original);
        double targetRate = getRate(target);
        return originalRate / targetRate;
    }

    private double getRate(Currency currency) {
        if (currency == Currency.BYN) {
            return 1;
        }

        try {
            JSONObject json = new JSONObject(getResponse(currency));
            double scale = json.getDouble("Cur_Scale");
            double rate = json.getDouble("Cur_OfficialRate");
            return rate / scale;
        } catch(IOException e) {
            CurrencyRate currencyRate = currencyRateRepository.findById(currency.getId()).get();
            double scale = currencyRate.getScale();
            double rate = currencyRate.getRate();
            log.error("Error occurred getting currency rate: " + e.getMessage() +
            "\nCurrency rate loaded from database.");
            return rate / scale;
        }
    }

    private String getResponse(Currency currency) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(NBRB_URL + currency.getId())
                .build();
        Response response = client.newCall(request).execute();
        return response.peekBody(2048).string();
    }

    @Scheduled(cron="${cron.scheduler.currency-rate}")
    private void saveCurrencyRates() {
        for(Currency currency: Currency.values()) {
            CurrencyRate currencyRate = new CurrencyRate();
            if(currency != Currency.BYN) {
                JSONObject json = null;
                try {
                    json = new JSONObject(getResponse(currency));
                } catch(IOException e) {
                    log.error("Error occurred: " + e.getMessage());
                    e.printStackTrace();
                }
                currencyRate.setId(currency.getId());
                currencyRate.setCurrency(currency);
                currencyRate.setScale(json.getDouble("Cur_Scale"));
                currencyRate.setRate(json.getDouble("Cur_OfficialRate"));
                currencyRate.setDate(new Timestamp(System.currentTimeMillis()));
                currencyRateRepository.save(currencyRate);
            }
        }
    }
}
