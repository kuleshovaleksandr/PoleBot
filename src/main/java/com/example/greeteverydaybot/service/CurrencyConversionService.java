package com.example.greeteverydaybot.service;

import com.example.greeteverydaybot.model.Currency;
import com.example.greeteverydaybot.service.impl.NbrbCurrencyConversionService;

public interface CurrencyConversionService {

    static CurrencyConversionService getInstance() {
        return new NbrbCurrencyConversionService();
    }

    double getConversionRatio(Currency original, Currency target);
}
