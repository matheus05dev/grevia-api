package com.projeto1cc.grevia.user.dto;

import com.projeto1cc.grevia.user.model.enums.Role;

public record UserResponseDTO(Long id, String name, String email, Role role) {
}
