package com.example.polebot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class UpdateHandlerFactory {

    @Autowired private ApplicationContext context;

    public UpdateHandler getUpdateHandler(UpdateHandlerStage updateHandlerStage) {
        UpdateHandler updateHandler = null;
        switch(updateHandlerStage) {
            case CALLBACK:
                updateHandler = (CallBackUpdateHandler) context.getBean("callBackUpdateHandler");
                break;
            case ANIMATION:
                updateHandler = (AnimationUpdateHandler) context.getBean("animationUpdateHandler");
                break;
            case STICKER:
                updateHandler = (StickerUpdateHandler) context.getBean("stickerUpdateHandler");
                break;
            case TEXT:
                updateHandler = (TextUpdateHandler) context.getBean("textUpdateHandler");
                break;
        }
        return updateHandler;
    }
}
