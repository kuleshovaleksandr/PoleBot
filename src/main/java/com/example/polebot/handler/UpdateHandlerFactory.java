package com.example.polebot.handler;

import org.springframework.stereotype.Component;

@Component
public class UpdateHandlerFactory {
    public UpdateHandler getUpdateHandler(UpdateHandlerStage updateHandlerStage) {
        UpdateHandler updateHandler = null;
        switch(updateHandlerStage) {
            case CALLBACK:
                updateHandler = new CallBackUpdateHandler();
                break;
            case ANIMATION:
                updateHandler = new AnimationUpdateHandler();
                break;
            case STICKER:
                updateHandler = new StickerUpdateHandler();
                break;
        }
        return updateHandler;
    }
}
