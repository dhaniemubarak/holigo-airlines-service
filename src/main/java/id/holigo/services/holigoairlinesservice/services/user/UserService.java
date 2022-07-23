package id.holigo.services.holigoairlinesservice.services.user;

import id.holigo.services.common.model.UserDtoForUser;

public interface UserService {

    UserDtoForUser getUser(Long userId);
}
