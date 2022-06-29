package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.Passport;
import id.holigo.services.holigoairlinesservice.web.model.PassportDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PassportMapper {

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Passport passportDtoToPassport(PassportDto passportDto);

    PassportDto passportToPassportDto(Passport passport);
}
