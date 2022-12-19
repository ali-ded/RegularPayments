package com.payments.regularpayments.mapper;

import com.payments.regularpayments.dto.PersonDto;
import com.payments.regularpayments.model.PersonEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    PersonEntity personDtoToPersonEntity(PersonDto personDto);
}
