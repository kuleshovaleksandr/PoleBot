package com.example.polebot.service.impl;

import com.example.polebot.entity.Animation;
import com.example.polebot.model.WeekDay;
import com.example.polebot.repository.AnimationRepository;
import com.example.polebot.service.AnimationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class DBAnimationService implements AnimationService {

    @Autowired
    private AnimationRepository animationRepository;

    public String getRandomWeekDayAnimation(WeekDay weekDay) {
        List<Animation> animations = animationRepository.findAnimationsByWeekDay(weekDay);
        return animations.get(new Random().nextInt(animations.size())).getFileId();
    }

    @Override
    public String getRandomAnimation(String weekDay) {
        return null;
    }

    @Override
    public String getAnimation() {
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
