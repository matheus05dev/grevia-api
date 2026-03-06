package com.projeto1cc.grevia.care.mapper;

import com.projeto1cc.grevia.care.model.CareRecord;
import com.projeto1cc.grevia.care.dto.CareRecordRequestDTO;
import com.projeto1cc.grevia.care.dto.CareRecordResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CareRecordMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "carePlan", ignore = true)
    CareRecord toEntity(CareRecordRequestDTO dto);

    @Mapping(source = "carePlan.id", target = "carePlanId")
    CareRecordResponseDTO toResponseDTO(CareRecord careRecord);
}
