package com.projeto1cc.grevia.user.mapper;

import com.projeto1cc.grevia.user.dto.UserRequestDTO;
import com.projeto1cc.grevia.user.dto.UserResponseDTO;
import com.projeto1cc.grevia.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "totalPoints", ignore = true)
    User toUser(UserRequestDTO dto);

    UserResponseDTO toUserResponseDTO(User user);
}
