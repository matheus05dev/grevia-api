package com.projeto1cc.grevia.plant.model;

import com.projeto1cc.grevia.plant.enums.SoilType;
import com.projeto1cc.grevia.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "plants")
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String species;

    @Column(length = 2000)
    private String recommendations;

    @Enumerated(EnumType.STRING)
    private SoilType soilType;

    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
