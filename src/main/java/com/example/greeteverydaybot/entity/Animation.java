package com.example.greeteverydaybot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="animations")
@Data
public class Animation {

    @Id
    private String id;

    @Column(name="name")
    private String name;
}
