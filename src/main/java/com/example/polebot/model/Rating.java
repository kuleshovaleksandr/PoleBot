package com.example.polebot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Rating {
    GENERAL("g"),
    PARENTAL_GUIDANCE_SUGGESTED("pg"),
    PARENTS_STRONGLY_CAUTIONED("pg-13"),
    RESTRICTED("r");

    private final String rate;
}
