package com.projeto1cc.grevia.care.repository;

import com.projeto1cc.grevia.care.CarePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarePlanRepository extends JpaRepository<CarePlan, Long> {
    List<CarePlan> findByPlantId(Long plantId);
}
