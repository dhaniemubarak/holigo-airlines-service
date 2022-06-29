package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.IdentityCard;
import id.holigo.services.holigoairlinesservice.web.model.IdentityCardDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface IdentityCardMapper {

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "passenger", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    IdentityCard identityCardDtoToIdentityCard(IdentityCardDto identityCardDto);

    IdentityCardDto identityCardToIdentityCardDto(IdentityCard identityCard);
}
