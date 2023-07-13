package com.example.polebot.service;

import com.example.polebot.entity.CurrencyRate;
import com.example.polebot.model.Currency;
import com.example.polebot.repository.CurrencyRateRepository;
import com.example.polebot.service.impl.NbrbCurrencyConversionService;
import org.glassfish.grizzly.http.util.TimeStamp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class NbrbCurrencyConversionServiceTest {

    @InjectMocks
    private NbrbCurrencyConversionService currencyConversionService;

    @Mock
    private CurrencyRateRepository currencyRateRepository;
    private CurrencyRate currencyRate;

    @BeforeEach
    public void initCurrencyRate() {
        currencyRate = new CurrencyRate(431, Currency.USD, 1, 2.5, new Timestamp(System.currentTimeMillis()));
    }

    @Test
    public void get_conversion_ratio() {
        Mockito.when(currencyRateRepository.findById(431)).thenReturn(Optional.of(currencyRate));

        double conversionRatio = currencyConversionService.getConversionRatio(Currency.BYN, Currency.USD);
        Mockito.verify(currencyConversionService, Mockito.atLeastOnce()).getConversionRatio(Currency.BYN, Currency.USD);
        Assertions.assertEquals(1.5, conversionRatio);
    }
}
