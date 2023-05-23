package com.example.polebot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NeuralLoveArtLayout {
    SQUARE("square"),
    VERTICAL("vertical"),
    HORIZONTAL("horizontal");

    private final String layout;
}
