package com.example.polebot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Language {
    RU("ru"), EN("en");

    private final String name;
}
