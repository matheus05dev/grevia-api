package com.projeto1cc.grevia.user.dto;

import com.projeto1cc.grevia.user.model.enums.Role;
import com.projeto1cc.grevia.user.model.enums.Status;
import java.time.LocalDate;

public record UserResponseDTO(String name, String email, Role role, Status status, LocalDate lastCareDate, Integer currentStreak, Integer totalCareActions, Integer totalPoints) {
}
