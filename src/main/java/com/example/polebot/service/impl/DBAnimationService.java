package com.example.polebot.service.impl;

import com.example.polebot.entity.Animation;
import com.example.polebot.repository.AnimationRepository;
import com.example.polebot.service.AnimationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DBAnimationService implements AnimationService {

    @Autowired
    private AnimationRepository animationRepository;

    @Override
    public String getRandomGif(String tag) {
        return null;
    }

    @Override
    public String searchGif() {
        return null;
    }

    @Override
    public void saveAnimation(String id, String fileId, String name) {
        Animation animation = new Animation();
        animation.setId(id);
        animation.setFileId(fileId);
        animation.setName(name);

        animationRepository.save(animation);
    }
}
