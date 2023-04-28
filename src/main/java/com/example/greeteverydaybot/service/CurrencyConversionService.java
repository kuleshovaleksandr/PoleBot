package com.example.greeteverydaybot.service;

import com.example.greeteverydaybot.model.Currency;

public interface CurrencyConversionService {

    static CurrencyConversionService getInstance() {
        return new NbrbCurrencyConversionService();
    }

    double getConversionRatio(Currency original, Currency target);
}
