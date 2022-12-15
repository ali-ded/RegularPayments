package com.payments.regularpayments.mapper;

import com.payments.regularpayments.dto.PersonDto;
import com.payments.regularpayments.model.PersonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    PersonDto personEntityToPersonDto(PersonEntity personEntity);

    PersonEntity personDtoToPersonEntity(PersonDto personDto);
}
