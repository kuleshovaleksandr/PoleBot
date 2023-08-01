package com.example.polebot.service;

import com.example.polebot.entity.Animation;
import com.example.polebot.model.WeekDay;
import com.example.polebot.repository.AnimationRepository;
import com.example.polebot.service.impl.DBAnimationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class DBAnimationServiceTest {

    @InjectMocks
    private DBAnimationService dbAnimationService;

    @Mock
    private AnimationRepository animationRepository;

    @Test
    public void get_random_weekday_animation() {
        List<Animation> animations = List.of(new Animation("1", "1", "greet1", WeekDay.MONDAY),
                new Animation("2", "2", "greet2", WeekDay.MONDAY),
                new Animation("3", "3", "greet3", WeekDay.MONDAY));
        Mockito.when(animationRepository.findAnimationsByWeekDay(WeekDay.MONDAY)).thenReturn(animations);
        Animation animation = dbAnimationService.getRandomWeekDayAnimation(WeekDay.MONDAY);
        assertThat(animations).hasSize(3)
                .contains(animation);
    }

    @Test
    public void save_animation() {
        Animation animation = new Animation("1", "1", "greet1", null);
        Mockito.when(animationRepository.save(animation)).thenReturn(animation);
        assertEquals(animation, dbAnimationService.saveAnimation("1", "1", "greet1"));
    }
}
