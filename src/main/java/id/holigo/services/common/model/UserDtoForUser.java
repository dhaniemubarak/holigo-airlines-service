package id.holigo.services.common.model;
import lombok.*;

import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDtoForUser implements Serializable {

    static final long serialVersionUID = -55181210L;
    private Long id;

    private String name;

    private String phoneNumber;

    UserParentDtoForUser parent;

    private String email;

    private EmailStatusEnum emailStatus;

    private AccountStatusEnum accountStatus;

    private String mobileToken;

    private String type;

    private UserGroupEnum userGroup;

    List<UserDeviceDto> userDevices;

}
