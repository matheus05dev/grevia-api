package com.projeto1cc.grevia.core.feedback.dto;

import java.time.LocalDateTime;

public record AppFeedbackResponseDTO(
    Long id,
    String category,
    String message,
    Long submittedById,
    String submittedByName,
    LocalDateTime submittedAt
) {}
