package com.example.polebot.service;

import com.example.polebot.model.Currency;
import com.example.polebot.service.impl.NbrbCurrencyConversionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NbrbCurrencyConversionServiceTest {

    @Mock
    private NbrbCurrencyConversionService currencyConversionService;

    @Test
    public void get_conversion_ratio() {
        Mockito.when(currencyConversionService.getConversionRatio(Currency.BYN, Currency.USD)).thenReturn(1.5);
        double conversionRatio = currencyConversionService.getConversionRatio(Currency.BYN, Currency.USD);
        Assertions.assertEquals(1.5, conversionRatio);
    }
}
