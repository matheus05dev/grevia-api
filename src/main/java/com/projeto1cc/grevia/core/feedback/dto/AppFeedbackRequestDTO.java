package com.projeto1cc.grevia.core.feedback.dto;

import jakarta.validation.constraints.NotBlank;

public record AppFeedbackRequestDTO(
    @NotBlank(message = "A categoria é obrigatória") String category,
    @NotBlank(message = "A mensagem é obrigatória") String message
) {}
