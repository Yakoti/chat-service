package com.ridetogether.chat_service;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rides")
@Data
public class Ride {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    public String name;
}
