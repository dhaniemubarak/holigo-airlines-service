package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.Airport;
import id.holigo.services.holigoairlinesservice.web.model.AirportDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AirportMapper {

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Airport airportDtoToAirport(AirportDto airportDto);

    AirportDto airportToAirportDto(Airport airport);
}
