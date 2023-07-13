package com.example.polebot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NeuralLoveArtStyle {
    PAINTING("painting"),
    FANTASY("fantasy"),
    ANIME("anime"),
    CYBERPUNK("cyberpunk"),
    NATURE("nature"),
    STEAMPUNK("steampunk"),
    SCI_FI("sci-fi"),
    SPACE("space"),
    CREEPY("creepy"),
    TATTOO("tattoo"),
    TEXTURE("texture"),
    ANYTHING("anything"),
    PHOTO("photo"),
    TAROT("tarot"),
    IMPASTO_OIL("impasto oil");

    private final String style;
}
