package com.example.polebot.entity;

import com.example.polebot.model.Currency;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
@Table(name="")
public class CurrencyRate {

    @Id
    private int id;

    @Column(name="currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name="scale")
    private double scale;

    @Column(name="rate")
    private double rate;

    @Column(name="date")
    private Timestamp date;
}
