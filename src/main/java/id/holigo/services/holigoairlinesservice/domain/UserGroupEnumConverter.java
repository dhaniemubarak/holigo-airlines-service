package id.holigo.services.holigoairlinesservice.domain;

import id.holigo.services.common.model.UserGroupEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class UserGroupEnumConverter implements AttributeConverter<UserGroupEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserGroupEnum attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getCode();
    }

    @Override
    public UserGroupEnum convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        return Stream.of(UserGroupEnum.values()).filter(c -> c.getCode().equals(dbData)).findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
