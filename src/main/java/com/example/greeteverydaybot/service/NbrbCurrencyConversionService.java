package com.example.greeteverydaybot.service;

import com.example.greeteverydaybot.model.Currency;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class NbrbCurrencyConversionService implements CurrencyConversionService {
    @Override
    public double getConversionRatio(Currency original, Currency target) {
        double originalRate = getRate(original);
        double targetRate = getRate(target);
        return originalRate / targetRate;
    }

    private double getRate(Currency currency) {
        if(currency == Currency.BYN) {
            return 1;
        }

        try {
            URL url = new URL("https://www.nbrb.by/api/exrates/rates/" + currency.getId());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONObject json = new JSONObject(response.toString());
            double scale = json.getDouble("Cur_Scale");
            double rate = json.getDouble("Cur_OfficialRate");
            return rate / scale;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
