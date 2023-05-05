package com.example.greeteverydaybot.repository;

import com.example.greeteverydaybot.entity.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StickerRepository extends JpaRepository<Sticker, String> {
    Optional<Sticker> findByEmoji(String emoji);
}
