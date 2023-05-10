package com.example.polebot.handler;

import com.example.polebot.service.impl.DBAnimationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.games.Animation;

@Component
public class AnimationUpdateHandler implements UpdateHandler {

    @Autowired private DBAnimationService animationService;

    @Override
    public void handleUpdate(Update update) {
        Animation animation = update.getMessage().getAnimation();
        String id = animation.getFileUniqueId();
        String fileId = animation.getFileId();
        String name = animation.getFileName();
        animationService.saveAnimation(id, fileId, name);
    }
}
