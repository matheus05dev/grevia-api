package com.projeto1cc.grevia.core.feedback.mapper;

import com.projeto1cc.grevia.core.feedback.model.AppFeedback;
import com.projeto1cc.grevia.core.feedback.dto.AppFeedbackRequestDTO;
import com.projeto1cc.grevia.core.feedback.dto.AppFeedbackResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppFeedbackMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "submittedBy", ignore = true)
    @Mapping(target = "submittedAt", ignore = true)
    AppFeedback toEntity(AppFeedbackRequestDTO dto);

    @Mapping(source = "submittedBy.id", target = "submittedById")
    @Mapping(source = "submittedBy.name", target = "submittedByName")
    AppFeedbackResponseDTO toResponseDTO(AppFeedback feedback);
}
