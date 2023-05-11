package com.example.polebot;

import com.example.polebot.config.BotConfig;
import com.example.polebot.entity.User;
import com.example.polebot.handler.UpdateHandlerFactory;
import com.example.polebot.handler.UpdateHandlerStage;
import com.example.polebot.model.Currency;
import com.example.polebot.model.WeekDay;
import com.example.polebot.parser.CommandParser;
import com.example.polebot.parser.TextParser;
import com.example.polebot.repository.UserRepository;
import com.example.polebot.sender.Sender;
import com.example.polebot.service.CurrencyConversionService;
import com.example.polebot.service.StickerService;
import com.example.polebot.service.impl.DBAnimationService;
import com.example.polebot.service.impl.GiphyAnimationService;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class PoleBot extends TelegramLongPollingBot {

    @Autowired private BotConfig botConfig;

    @Autowired private UpdateHandlerFactory updateHandlerFactory;
    @Autowired private UserRepository userRepository;
    @Autowired private GiphyAnimationService giphyAnimationService;
    @Autowired private StickerService stickerService;

    private List<BotCommand> listOfCommands;

    @PostConstruct
    public void initCommands() {
        listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "give a welcome message"));
        listOfCommands.add(new BotCommand("/mydata", "get your data stored"));
        listOfCommands.add(new BotCommand("/currency", "change currency"));

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            updateHandlerFactory.getUpdateHandler(UpdateHandlerStage.TEXT)
                    .handleUpdate(update);
        } else if(update.hasCallbackQuery()) {
            updateHandlerFactory.getUpdateHandler(UpdateHandlerStage.CALLBACK)
                    .handleUpdate(update);
        } else if(update.getMessage().hasAnimation()) {
            updateHandlerFactory.getUpdateHandler(UpdateHandlerStage.ANIMATION)
                    .handleUpdate(update);
        } else if(update.getMessage().hasSticker()) {
            updateHandlerFactory.getUpdateHandler(UpdateHandlerStage.STICKER)
                    .handleUpdate(update);
        }
    }

    @SneakyThrows
    private void sendGif(long chatId, String tag) {
        String gifUrl = giphyAnimationService.getRandomAnimation(tag);
        SendAnimation animation = SendAnimation.builder()
                .chatId(chatId)
                .animation(new InputFile(gifUrl))
                .build();
        try {
            execute(animation);
        } catch(TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    private void register(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Do you really want to register?");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        var yesButton = new InlineKeyboardButton();
        yesButton.setText("Yes");
        yesButton.setCallbackData("YES_BUTTON");
        rowInline.add(yesButton);

        var noButton = new InlineKeyboardButton();
        noButton.setText("No");
        noButton.setCallbackData("NO_BUTTON");
        rowInline.add(noButton);

        rowsInline.add(rowInline);
        inlineKeyboardMarkup.setKeyboard(rowsInline);
        message.setReplyMarkup(inlineKeyboardMarkup);

        execute(message);
    }

    private void registerUser(Message message) {
        if(userRepository.findById(message.getChatId()).isEmpty()) {
            long id = message.getChatId();
            Chat chat = message.getChat();

            User user = new User();
            user.setId(id);
            user.setUserName(chat.getUserName());
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
            log.info("User created: " + user);
        }
    }

    private void findWordTesla(long chatId, String messageText) {
        Pattern pattern = Pattern.compile("[Тт]есл.");
        Matcher matcher = pattern.matcher(messageText);

        while(matcher.find()) {
            sendMessage(chatId, "you are boring");
        }
    }

    @SneakyThrows
    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(textToSend)
                .build();
        execute(message);
    }

    @SneakyThrows
    private void addKeyBoardMarkup() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("weather");
        row.add("get a joke");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("register");
        row.add("check my data");
        row.add("delete my data");
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
//        message.setReplyMarkup(keyboardMarkup);
//        execute(message);
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }
}
