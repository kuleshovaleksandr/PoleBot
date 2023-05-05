package com.example.polebot.service;

import com.example.polebot.model.Currency;
import com.example.polebot.service.impl.NbrbCurrencyConversionService;

public interface CurrencyConversionService {

    static CurrencyConversionService getInstance() {
        return new NbrbCurrencyConversionService();
    }

    double getConversionRatio(Currency original, Currency target);
}
