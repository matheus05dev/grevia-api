package com.projeto1cc.grevia.user.mapper;

import com.projeto1cc.grevia.user.dto.UserRequestDTO;
import com.projeto1cc.grevia.user.dto.UserResponseDTO;
import com.projeto1cc.grevia.user.model.User;
import com.projeto1cc.grevia.user.model.enums.GardenerLevel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "totalPoints", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toUser(UserRequestDTO dto);

    default UserResponseDTO toUserResponseDTO(User user) {
        if (user == null) return null;

        GardenerLevel level = GardenerLevel.fromPoints(
            user.getTotalPoints() != null ? user.getTotalPoints() : 0
        );

        return new UserResponseDTO(
            user.getName(),
            user.getEmail(),
            user.getRole(),
            user.getStatus(),
            user.getLastCareDate(),
            user.getCurrentStreak(),
            user.getTotalCareActions(),
            user.getTotalPoints(),
            level.getFullTitle(),
            level.getEmoji(),
            level.getLevel()
        );
    }
}
