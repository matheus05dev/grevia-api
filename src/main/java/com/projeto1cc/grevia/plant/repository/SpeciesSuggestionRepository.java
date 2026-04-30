package com.projeto1cc.grevia.plant.repository;

import com.projeto1cc.grevia.plant.model.SpeciesSuggestion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpeciesSuggestionRepository extends JpaRepository<SpeciesSuggestion, Long> {

    @Override
    @EntityGraph(attributePaths = "submittedBy")
    List<SpeciesSuggestion> findAll();

    @Override
    @EntityGraph(attributePaths = "submittedBy")
    Optional<SpeciesSuggestion> findById(Long id);
}
