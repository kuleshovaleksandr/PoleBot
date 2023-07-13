package com.example.polebot.service;

import com.example.polebot.model.Currency;
import com.example.polebot.service.impl.NbrbCurrencyConversionService;

public interface CurrencyConversionService {

    double getConversionRatio(Currency original, Currency target);
}
