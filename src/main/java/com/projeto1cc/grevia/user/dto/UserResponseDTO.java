package com.projeto1cc.grevia.user.dto;

import com.projeto1cc.grevia.user.model.enums.Role;
import com.projeto1cc.grevia.user.model.enums.Status;

public record UserResponseDTO(Long id, String name, String email, Role role, Status status) {
}
