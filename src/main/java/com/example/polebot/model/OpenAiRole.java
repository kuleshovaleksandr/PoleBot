package com.example.polebot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OpenAiRole {
    ASSISTANT("assistant"),
    USER("user"),
    SYSTEM("system");

    private final String role;
}
