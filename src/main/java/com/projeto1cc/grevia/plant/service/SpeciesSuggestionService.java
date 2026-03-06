package com.projeto1cc.grevia.plant.service;

import com.projeto1cc.grevia.plant.model.SpeciesSuggestion;
import com.projeto1cc.grevia.plant.dto.SpeciesSuggestionRequestDTO;
import com.projeto1cc.grevia.plant.dto.SpeciesSuggestionResponseDTO;
import com.projeto1cc.grevia.plant.mapper.SpeciesSuggestionMapper;
import com.projeto1cc.grevia.plant.repository.SpeciesSuggestionRepository;
import com.projeto1cc.grevia.user.model.User;
import com.projeto1cc.grevia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpeciesSuggestionService {

    private final SpeciesSuggestionRepository repository;
    private final SpeciesSuggestionMapper mapper;
    private final UserRepository userRepository;

    @Transactional
    public SpeciesSuggestionResponseDTO submitSuggestion(SpeciesSuggestionRequestDTO requestDTO, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        SpeciesSuggestion suggestion = mapper.toEntity(requestDTO);
        suggestion.setSubmittedBy(user);
        suggestion.setSubmittedAt(LocalDateTime.now());

        SpeciesSuggestion savedSuggestion = repository.save(suggestion);
        return mapper.toResponseDTO(savedSuggestion);
    }

    @Transactional(readOnly = true)
    public List<SpeciesSuggestionResponseDTO> getAllSuggestions() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
