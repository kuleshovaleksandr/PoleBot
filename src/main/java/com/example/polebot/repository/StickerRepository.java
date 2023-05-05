package com.example.polebot.repository;

import com.example.polebot.entity.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StickerRepository extends JpaRepository<Sticker, String> {
    Optional<Sticker> findByEmoji(String emoji);
}
