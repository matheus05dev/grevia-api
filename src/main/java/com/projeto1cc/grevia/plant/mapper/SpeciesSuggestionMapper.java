package com.projeto1cc.grevia.plant.mapper;

import com.projeto1cc.grevia.plant.model.SpeciesSuggestion;
import com.projeto1cc.grevia.plant.dto.SpeciesSuggestionRequestDTO;
import com.projeto1cc.grevia.plant.dto.SpeciesSuggestionResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SpeciesSuggestionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "submittedBy", ignore = true)
    @Mapping(target = "submittedAt", ignore = true)
    SpeciesSuggestion toEntity(SpeciesSuggestionRequestDTO dto);

    @Mapping(source = "submittedBy.id", target = "submittedById")
    SpeciesSuggestionResponseDTO toResponseDTO(SpeciesSuggestion suggestion);
}
