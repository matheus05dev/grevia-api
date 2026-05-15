package com.projeto1cc.grevia.plant.repository;

import com.projeto1cc.grevia.plant.model.Plant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {

    @EntityGraph(attributePaths = "user")
    List<Plant> findByUserId(Long userId);

    @Override
    @EntityGraph(attributePaths = "user")
    List<Plant> findAll();

    @Override
    @EntityGraph(attributePaths = "user")
    Optional<Plant> findById(Long id);

    @EntityGraph(attributePaths = "user")
    List<Plant> findByUserIdAndStatus(Long userId, com.projeto1cc.grevia.plant.enums.PlantStatus status);

    @EntityGraph(attributePaths = {"user", "carePlans"})
    org.springframework.data.domain.Page<Plant> findByUserIdAndStatusIn(
        Long userId, 
        List<com.projeto1cc.grevia.plant.enums.PlantStatus> statuses, 
        org.springframework.data.domain.Pageable pageable
    );

    @EntityGraph(attributePaths = {"user", "carePlans"})
    org.springframework.data.domain.Page<Plant> findByUserId(Long userId, org.springframework.data.domain.Pageable pageable);

    void deleteByUserId(Long userId);
}
