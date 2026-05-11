package com.projeto1cc.grevia.core.feedback.service;

import com.projeto1cc.grevia.core.feedback.model.AppFeedback;
import com.projeto1cc.grevia.core.feedback.dto.AppFeedbackRequestDTO;
import com.projeto1cc.grevia.core.feedback.dto.AppFeedbackResponseDTO;
import com.projeto1cc.grevia.core.feedback.mapper.AppFeedbackMapper;
import com.projeto1cc.grevia.core.feedback.repository.AppFeedbackRepository;
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
public class AppFeedbackService {

    private final AppFeedbackRepository repository;
    private final AppFeedbackMapper mapper;
    private final UserRepository userRepository;

    @Transactional
    public AppFeedbackResponseDTO submitFeedback(AppFeedbackRequestDTO requestDTO, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        AppFeedback feedback = mapper.toEntity(requestDTO);
        feedback.setSubmittedBy(user);
        feedback.setSubmittedAt(LocalDateTime.now());

        AppFeedback savedFeedback = repository.save(feedback);
        return mapper.toResponseDTO(savedFeedback);
    }

    @Transactional(readOnly = true)
    public List<AppFeedbackResponseDTO> getAllFeedbacks() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
