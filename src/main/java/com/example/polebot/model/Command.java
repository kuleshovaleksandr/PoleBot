package com.example.polebot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Command {
    INFO("/info", "give info about bot commands"),
    CURRENCY("/currency", "change currency");

    private final String name;
    private final String desc;

    public static List<BotCommand> getBotCommands() {
        return Arrays.stream(values())
                .map(command -> new BotCommand(command.getName(), command.getDesc()))
                .collect(Collectors.toList());
    }
}
