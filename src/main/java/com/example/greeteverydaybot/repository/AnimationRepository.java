package com.example.greeteverydaybot.repository;

import com.example.greeteverydaybot.entity.Animation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimationRepository extends JpaRepository<Animation, String> {
}
