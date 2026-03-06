package com.projeto1cc.grevia.care;

import com.projeto1cc.grevia.care.enums.CareType;
import com.projeto1cc.grevia.care.enums.FrequencyType;
import com.projeto1cc.grevia.plant.Plant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "care_plans")
public class CarePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CareType careType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FrequencyType frequencyType;

    @Column(nullable = false)
    private LocalDate nextCareDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;
}
