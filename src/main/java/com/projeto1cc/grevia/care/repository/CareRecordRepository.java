package com.projeto1cc.grevia.care.repository;

import com.projeto1cc.grevia.care.CareRecord;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CareRecordRepository extends JpaRepository<CareRecord, Long> {

    @EntityGraph(attributePaths = "carePlan")
    List<CareRecord> findByCarePlanId(Long carePlanId);

    @Override
    @EntityGraph(attributePaths = {"carePlan", "carePlan.plant", "carePlan.plant.user"})
    Optional<CareRecord> findById(Long id);
}
