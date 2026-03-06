package com.projeto1cc.grevia.plant.repository;

import com.projeto1cc.grevia.plant.model.SpeciesSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeciesSuggestionRepository extends JpaRepository<SpeciesSuggestion, Long> {
}
