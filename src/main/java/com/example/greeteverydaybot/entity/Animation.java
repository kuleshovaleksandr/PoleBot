package com.example.greeteverydaybot.entity;

import com.example.greeteverydaybot.model.WeekDay;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="animations")
@Data
public class Animation {

    @Id
    private String id;

    @Column(name="file_id")
    private String fileId;

    @Column(name="name")
    private String name;

    @Column(name="day")
    @Enumerated(EnumType.STRING)
    private WeekDay weekDay;
}