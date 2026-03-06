package com.projeto1cc.grevia.care.repository;

import com.projeto1cc.grevia.care.model.CarePlan;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarePlanRepository extends JpaRepository<CarePlan, Long> {

    @EntityGraph(attributePaths = "plant")
    List<CarePlan> findByPlantId(Long plantId);

    @Override
    @EntityGraph(attributePaths = {"plant", "plant.user"})
    Optional<CarePlan> findById(Long id);
}
