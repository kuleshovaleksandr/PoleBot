package com.example.polebot.entity;

import com.example.polebot.model.WeekDay;
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

    @Column(name="week_day")
    @Enumerated(EnumType.STRING)
    private WeekDay weekDay;
}
