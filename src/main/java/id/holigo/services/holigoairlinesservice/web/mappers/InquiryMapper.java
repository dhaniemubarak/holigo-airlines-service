package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.Inquiry;
import id.holigo.services.holigoairlinesservice.web.model.InquiryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {AirportMapper.class})
public interface InquiryMapper {

    @Mapping(target = "userId", ignore = true)
    InquiryDto inquiryToInquiryDto(Inquiry inquiry);

    @Mapping(target = "originAirport", ignore = true)
    @Mapping(target = "destinationAirport", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Inquiry inquiryDtoToInquiry(InquiryDto inquiryDto);
}
