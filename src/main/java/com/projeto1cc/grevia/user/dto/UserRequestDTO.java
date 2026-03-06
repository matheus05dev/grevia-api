package com.projeto1cc.grevia.user.dto;

import com.projeto1cc.grevia.user.model.enums.Role;
import com.projeto1cc.grevia.user.model.enums.Status;

public record UserRequestDTO(String name, String email, String password, Role role, Status status) {
}
