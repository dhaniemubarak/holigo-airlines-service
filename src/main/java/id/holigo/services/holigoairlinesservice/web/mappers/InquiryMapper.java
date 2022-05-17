package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.Inquiry;
import id.holigo.services.holigoairlinesservice.web.model.InquiryDto;
import org.mapstruct.Mapper;

@Mapper
public interface InquiryMapper {

    InquiryDto inquiryToInquiryDto(Inquiry inquiry);

    Inquiry inquiryDtoToInquiry(InquiryDto inquiryDto);
}
