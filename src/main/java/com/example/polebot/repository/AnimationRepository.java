package com.example.polebot.repository;

import com.example.polebot.entity.Animation;
import com.example.polebot.model.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimationRepository extends JpaRepository<Animation, String> {
    List<Animation> findAnimationsByWeekDay(WeekDay weekDay);
}
