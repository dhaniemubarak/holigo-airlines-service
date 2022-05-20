package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.ContactPerson;
import id.holigo.services.holigoairlinesservice.web.model.ContactPersonDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ContactPersonMapper {

    ContactPersonDto contactPersonToContactPersonDto(ContactPerson contactPerson);

    @Mapping(target = "createdAt", ignore = true)
    ContactPerson contactPersonDtoToContactPerson(ContactPersonDto contactPersonDto);
}
