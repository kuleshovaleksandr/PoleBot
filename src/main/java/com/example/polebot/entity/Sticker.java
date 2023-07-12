package com.example.polebot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name="stickers")
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class Sticker {

    @Id
    private String id;

    @Column(name="file_id")
    private String fileId;

    @Column(name="name")
    private String name;

    @Column(name="emoji")
    private String emoji;
}
