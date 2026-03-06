package com.projeto1cc.grevia.care.repository;

import com.projeto1cc.grevia.care.CareRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareRecordRepository extends JpaRepository<CareRecord, Long> {
    List<CareRecord> findByCarePlanId(Long carePlanId);
}
