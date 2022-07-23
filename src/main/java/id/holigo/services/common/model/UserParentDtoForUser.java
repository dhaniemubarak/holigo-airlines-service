package id.holigo.services.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Null;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserParentDtoForUser implements Serializable {

    static final long serialVersionUID = -65181210L;
    @Null
    private Long id;

    @Null
    private Long officialId;

    private String name;

    private String referral;
}
