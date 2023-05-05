package com.example.polebot.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="ads_table")
@Data
public class Ads {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String text;
}
