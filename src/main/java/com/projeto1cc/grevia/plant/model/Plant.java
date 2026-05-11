package com.projeto1cc.grevia.plant.model;

import com.projeto1cc.grevia.plant.enums.SoilType;
import com.projeto1cc.grevia.plant.enums.Species;
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

    @Enumerated(EnumType.STRING)
    private Species species;

    @Column(name = "custom_species_name")
    private String customSpeciesName;

    @Column(length = 2000)
    private String recommendations;

    @Enumerated(EnumType.STRING)
    private SoilType soilType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private com.projeto1cc.grevia.plant.enums.PlantStatus status = com.projeto1cc.grevia.plant.enums.PlantStatus.ACTIVE;

    @Column(name = "registered_at", updatable = false)
    private java.time.LocalDate registeredAt;

    @Column(name = "harvested_at")
    private java.time.LocalDate harvestedAt;

    @Column(name = "archived_at")
    private java.time.LocalDate archivedAt;

    @Column(name = "history_notes", length = 1000)
    private String historyNotes;

    @Column(name = "space_type")
    private String spaceType;

    @Column(name = "space_size")
    private String spaceSize;

    @Column(name = "sun_hours")
    private Integer sunHours;

    @PrePersist
    protected void onCreate() {
        if (this.registeredAt == null) {
            this.registeredAt = java.time.LocalDate.now();
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<com.projeto1cc.grevia.care.model.CarePlan> carePlans = new java.util.ArrayList<>();
}
