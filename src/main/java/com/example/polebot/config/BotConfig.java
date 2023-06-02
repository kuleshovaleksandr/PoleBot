package com.example.polebot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@PropertySource("application.properties")
@Data
public class BotConfig {

    @Value("${bot.pole.name}")
    String botName;

    @Value("${bot.pole.token}")
    String token;
}
