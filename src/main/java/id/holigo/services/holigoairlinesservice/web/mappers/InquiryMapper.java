package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.Inquiry;
import id.holigo.services.holigoairlinesservice.web.model.InquiryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface InquiryMapper {

    InquiryDto inquiryToInquiryDto(Inquiry inquiry);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Inquiry inquiryDtoToInquiry(InquiryDto inquiryDto);
}
