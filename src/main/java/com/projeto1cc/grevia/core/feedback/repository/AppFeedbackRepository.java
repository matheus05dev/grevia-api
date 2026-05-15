package com.projeto1cc.grevia.core.feedback.repository;

import com.projeto1cc.grevia.core.feedback.model.AppFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppFeedbackRepository extends JpaRepository<AppFeedback, Long> {
    void deleteBySubmittedById(Long userId);
}
